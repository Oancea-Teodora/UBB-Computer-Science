import * as speakeasy from 'speakeasy';
import * as QRCode from 'qrcode';
import * as crypto from 'crypto';
import * as nodemailer from 'nodemailer';
import { User } from '../entity/User';

export class TwoFactorService {
    private static readonly APP_NAME = 'MovieApp';
    private static readonly BACKUP_CODES_COUNT = 10;

    /**
     * Generate a new 2FA secret for a user
     */
    static generateSecret(userEmail: string): { secret: string; otpauthUrl: string } {
        const secret = speakeasy.generateSecret({
            name: userEmail,
            issuer: this.APP_NAME,
            length: 32,
        });

        return {
            secret: secret.base32!,
            otpauthUrl: secret.otpauth_url!,
        };
    }

    /**
     * Generate QR code data URL for the secret
     */
    static async generateQRCode(otpauthUrl: string): Promise<string> {
        try {
            return await QRCode.toDataURL(otpauthUrl);
        } catch (error) {
            throw new Error('Failed to generate QR code');
        }
    }

    /**
     * Verify a TOTP token
     */
    static verifyToken(secret: string, token: string, window: number = 1): boolean {
        return speakeasy.totp.verify({
            secret: secret,
            encoding: 'base32',
            token: token,
            window: window, // Allow for time drift
        });
    }

    /**
     * Generate backup codes for account recovery
     */
    static generateBackupCodes(): string[] {
        const codes: string[] = [];
        for (let i = 0; i < this.BACKUP_CODES_COUNT; i++) {
            // Generate 8-character alphanumeric codes
            const code = crypto.randomBytes(4).toString('hex').toUpperCase();
            codes.push(code);
        }
        return codes;
    }

    /**
     * Verify a backup code and remove it from the user's list
     */
    static verifyBackupCode(user: User, inputCode: string): boolean {
        if (!user.twoFactorBackupCodes) {
            return false;
        }

        const codeIndex = user.twoFactorBackupCodes.indexOf(inputCode.toUpperCase());
        if (codeIndex === -1) {
            return false;
        }

        // Remove the used backup code
        user.twoFactorBackupCodes.splice(codeIndex, 1);
        return true;
    }

    /**
     * Send 2FA setup email with backup codes
     */
    static async sendSetupEmail(
        userEmail: string,
        backupCodes: string[],
        transportConfig?: any
    ): Promise<void> {
        if (!transportConfig) {
            console.log('Email transport not configured, skipping 2FA setup email');
            return;
        }

        const transporter = nodemailer.createTransport(transportConfig);

        const htmlContent = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>2FA Setup Complete</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #1976d2; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .backup-codes { background-color: #fff; padding: 15px; border: 1px solid #ddd; margin: 10px 0; }
                    .backup-code { font-family: monospace; font-size: 14px; padding: 5px; background-color: #f0f0f0; margin: 5px; display: inline-block; }
                    .warning { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; margin: 15px 0; border-radius: 4px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Two-Factor Authentication Setup Complete</h1>
                    </div>
                    <div class="content">
                        <p>Hello,</p>
                        <p>Two-factor authentication has been successfully enabled for your ${this.APP_NAME} account.</p>
                        
                        <div class="warning">
                            <h3>⚠️ Important: Save Your Backup Codes</h3>
                            <p>These backup codes can be used to access your account if you lose your authenticator device. Each code can only be used once.</p>
                        </div>
                        
                        <div class="backup-codes">
                            <h3>Your Backup Codes:</h3>
                            ${backupCodes.map(code => `<span class="backup-code">${code}</span>`).join('')}
                        </div>
                        
                        <div class="warning">
                            <p><strong>Store these codes in a safe place:</strong></p>
                            <ul>
                                <li>Save them in a password manager</li>
                                <li>Print them and store in a secure location</li>
                                <li>DO NOT store them on your computer or phone</li>
                            </ul>
                        </div>
                        
                        <p>If you did not enable 2FA on your account, please contact support immediately.</p>
                        
                        <p>Best regards,<br>The ${this.APP_NAME} Team</p>
                    </div>
                </div>
            </body>
            </html>
        `;

        const mailOptions = {
            from: process.env.SMTP_FROM || 'noreply@movieapp.com',
            to: userEmail,
            subject: `${this.APP_NAME} - Two-Factor Authentication Setup Complete`,
            html: htmlContent,
        };

        try {
            await transporter.sendMail(mailOptions);
            console.log(`2FA setup email sent to ${userEmail}`);
        } catch (error) {
            console.error('Failed to send 2FA setup email:', error);
            throw new Error('Failed to send setup email');
        }
    }

    /**
     * Send email with backup code when used
     */
    static async sendBackupCodeUsedEmail(
        userEmail: string,
        remainingCodes: number,
        transportConfig?: any
    ): Promise<void> {
        if (!transportConfig) {
            console.log('Email transport not configured, skipping backup code notification');
            return;
        }

        const transporter = nodemailer.createTransport(transportConfig);

        const htmlContent = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>Backup Code Used</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #ff9800; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .warning { background-color: ${remainingCodes <= 3 ? '#ffebee' : '#fff3cd'}; 
                              border: 1px solid ${remainingCodes <= 3 ? '#f44336' : '#ffeaa7'}; 
                              padding: 15px; margin: 15px 0; border-radius: 4px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Backup Code Used</h1>
                    </div>
                    <div class="content">
                        <p>Hello,</p>
                        <p>A backup code was just used to access your ${this.APP_NAME} account.</p>
                        
                        <div class="warning">
                            <p><strong>Remaining backup codes: ${remainingCodes}</strong></p>
                            ${remainingCodes <= 3 ?
                '<p>⚠️ <strong>Warning:</strong> You are running low on backup codes. Consider generating new ones in your account settings.</p>' :
                '<p>If you have lost access to your authenticator app, you can use the remaining backup codes to access your account.</p>'
            }
                        </div>
                        
                        <p>If you did not use this backup code, please:</p>
                        <ul>
                            <li>Change your password immediately</li>
                            <li>Review your account security settings</li>
                            <li>Contact support if you suspect unauthorized access</li>
                        </ul>
                        
                        <p>Best regards,<br>The ${this.APP_NAME} Team</p>
                    </div>
                </div>
            </body>
            </html>
        `;

        const mailOptions = {
            from: process.env.SMTP_FROM || 'noreply@movieapp.com',
            to: userEmail,
            subject: `${this.APP_NAME} - Backup Code Used`,
            html: htmlContent,
        };

        try {
            await transporter.sendMail(mailOptions);
            console.log(`Backup code notification sent to ${userEmail}`);
        } catch (error) {
            console.error('Failed to send backup code notification:', error);
        }
    }

    /**
     * Get email transport configuration from environment variables
     */
    static getEmailTransportConfig(): any {
        const smtpHost = process.env.SMTP_HOST;
        const smtpPort = process.env.SMTP_PORT;
        const smtpUser = process.env.SMTP_USER;
        const smtpPass = process.env.SMTP_PASS;

        if (!smtpHost || !smtpPort || !smtpUser || !smtpPass) {
            return null;
        }

        return {
            host: smtpHost,
            port: parseInt(smtpPort),
            secure: smtpPort === '465',
            auth: {
                user: smtpUser,
                pass: smtpPass,
            },
        };
    }
} 