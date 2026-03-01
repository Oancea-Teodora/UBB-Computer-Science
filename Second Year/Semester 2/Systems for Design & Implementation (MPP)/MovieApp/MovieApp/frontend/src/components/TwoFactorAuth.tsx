import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { apiFetch } from '../utils/api';

interface TwoFactorStatus {
    twoFactorEnabled: boolean;
    backupCodesRemaining: number;
    backupEmail: string;
    setupInProgress: boolean;
}

interface SetupResponse {
    secret: string;
    qrCode: string;
    manualEntryKey: string;
    instructions: {
        step1: string;
        step2: string;
        step3: string;
    };
}

interface VerifyResponse {
    message: string;
    backupCodes: string[];
    warning: string;
    instructions: {
        usage: string;
        security: string;
        regeneration: string;
    };
}

export const TwoFactorAuth: React.FC = () => {
    const { user } = useAuth();
    const [status, setStatus] = useState<TwoFactorStatus | null>(null);
    const [setupData, setSetupData] = useState<SetupResponse | null>(null);
    const [verificationCode, setVerificationCode] = useState('');
    const [disablePassword, setDisablePassword] = useState('');
    const [disableCode, setDisableCode] = useState('');
    const [backupCodes, setBackupCodes] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        fetchStatus();
    }, []);

    const fetchStatus = async () => {
        try {
            const response = await apiFetch<TwoFactorStatus>('/auth/2fa/status');
            setStatus(response);
        } catch (err: any) {
            setError(err.message);
        }
    };

    const startSetup = async () => {
        setLoading(true);
        setError('');
        try {
            const response = await apiFetch<SetupResponse>('/auth/2fa/setup', {
                method: 'POST'
            });
            setSetupData(response);
            setSuccess('2FA setup started! Scan the QR code with your authenticator app.');
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const verifySetup = async () => {
        if (verificationCode.length !== 6) {
            setError('Please enter a 6-digit code');
            return;
        }

        setLoading(true);
        setError('');
        try {
            const response = await apiFetch<VerifyResponse>('/auth/2fa/verify-setup', {
                method: 'POST',
                body: JSON.stringify({ token: verificationCode })
            });
            setBackupCodes(response.backupCodes);
            setSuccess(response.message);
            setSetupData(null);
            setVerificationCode('');
            await fetchStatus();
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const disable2FA = async () => {
        if (!disablePassword) {
            setError('Password is required');
            return;
        }

        setLoading(true);
        setError('');
        try {
            await apiFetch('/auth/2fa/disable', {
                method: 'POST',
                body: JSON.stringify({
                    password: disablePassword,
                    token: disableCode || undefined
                })
            });
            setSuccess('Two-factor authentication has been disabled');
            setDisablePassword('');
            setDisableCode('');
            await fetchStatus();
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    if (!user) {
        return <div className="p-4 text-center">Please log in to manage two-factor authentication.</div>;
    }

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-lg">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Two-Factor Authentication</h2>

            {error && (
                <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
                    {error}
                </div>
            )}

            {success && (
                <div className="mb-4 p-4 bg-green-100 border border-green-400 text-green-700 rounded">
                    {success}
                </div>
            )}

            {status && (
                <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded">
                    <h3 className="font-semibold mb-2">Current Status</h3>
                    <p><strong>2FA Enabled:</strong> {status.twoFactorEnabled ? '✅ Yes' : '❌ No'}</p>
                    {status.twoFactorEnabled && (
                        <>
                            <p><strong>Backup Codes Remaining:</strong> {status.backupCodesRemaining}</p>
                            <p><strong>Backup Email:</strong> {status.backupEmail}</p>
                        </>
                    )}
                    {status.setupInProgress && (
                        <p className="text-orange-600"><strong>Setup in Progress:</strong> Please complete verification</p>
                    )}
                </div>
            )}

            {/* Setup 2FA */}
            {!status?.twoFactorEnabled && !setupData && (
                <div className="mb-6">
                    <h3 className="text-lg font-semibold mb-3">Enable Two-Factor Authentication</h3>
                    <p className="text-gray-600 mb-4">
                        Add an extra layer of security to your account with two-factor authentication.
                    </p>
                    <button
                        onClick={startSetup}
                        disabled={loading}
                        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                    >
                        {loading ? 'Starting Setup...' : 'Start 2FA Setup'}
                    </button>
                </div>
            )}

            {/* QR Code and Verification */}
            {setupData && (
                <div className="mb-6">
                    <h3 className="text-lg font-semibold mb-3">Complete 2FA Setup</h3>

                    <div className="mb-4">
                        <h4 className="font-medium mb-2">Step 1: Scan QR Code</h4>
                        <p className="text-sm text-gray-600 mb-2">{setupData.instructions.step1}</p>
                        <div className="bg-gray-50 p-4 rounded border text-center">
                            <img
                                src={setupData.qrCode}
                                alt="2FA QR Code"
                                className="mx-auto mb-2"
                                style={{ maxWidth: '200px' }}
                            />
                            <p className="text-xs text-gray-500 break-all">
                                Manual entry key: <code className="bg-gray-200 px-1 rounded">{setupData.manualEntryKey}</code>
                            </p>
                        </div>
                    </div>

                    <div className="mb-4">
                        <h4 className="font-medium mb-2">Step 2: Enter Verification Code</h4>
                        <p className="text-sm text-gray-600 mb-2">{setupData.instructions.step3}</p>
                        <div className="flex gap-2">
                            <input
                                type="text"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
                                placeholder="Enter 6-digit code"
                                className="border border-gray-300 rounded px-3 py-2 flex-1"
                                maxLength={6}
                            />
                            <button
                                onClick={verifySetup}
                                disabled={loading || verificationCode.length !== 6}
                                className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 disabled:opacity-50"
                            >
                                {loading ? 'Verifying...' : 'Verify & Enable'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Backup Codes Display */}
            {backupCodes.length > 0 && (
                <div className="mb-6 p-4 bg-yellow-50 border border-yellow-200 rounded">
                    <h3 className="text-lg font-semibold mb-3 text-yellow-800">⚠️ Save Your Backup Codes</h3>
                    <p className="text-sm text-yellow-700 mb-3">
                        Save these backup codes in a secure location. Each code can only be used once.
                    </p>
                    <div className="grid grid-cols-2 gap-2 mb-3">
                        {backupCodes.map((code, index) => (
                            <code key={index} className="bg-white p-2 rounded border text-center font-mono">
                                {code}
                            </code>
                        ))}
                    </div>
                    <button
                        onClick={() => {
                            navigator.clipboard.writeText(backupCodes.join('\n'));
                            setSuccess('Backup codes copied to clipboard!');
                        }}
                        className="bg-yellow-600 text-white px-3 py-1 rounded text-sm hover:bg-yellow-700"
                    >
                        Copy All Codes
                    </button>
                </div>
            )}

            {/* Disable 2FA */}
            {status?.twoFactorEnabled && (
                <div className="mb-6 border-t pt-6">
                    <h3 className="text-lg font-semibold mb-3 text-red-600">Disable Two-Factor Authentication</h3>
                    <p className="text-gray-600 mb-4">
                        This will remove the extra security layer from your account.
                    </p>

                    <div className="space-y-3">
                        <input
                            type="password"
                            value={disablePassword}
                            onChange={(e) => setDisablePassword(e.target.value)}
                            placeholder="Enter your password"
                            className="w-full border border-gray-300 rounded px-3 py-2"
                        />
                        <input
                            type="text"
                            value={disableCode}
                            onChange={(e) => setDisableCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
                            placeholder="Enter 6-digit authenticator code"
                            className="w-full border border-gray-300 rounded px-3 py-2"
                            maxLength={6}
                        />
                        <button
                            onClick={disable2FA}
                            disabled={loading || !disablePassword}
                            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 disabled:opacity-50"
                        >
                            {loading ? 'Disabling...' : 'Disable 2FA'}
                        </button>
                    </div>
                </div>
            )}

            {/* Instructions */}
            <div className="mt-6 p-4 bg-gray-50 rounded">
                <h3 className="font-semibold mb-2">How to Test 2FA</h3>
                <ol className="text-sm text-gray-600 space-y-1">
                    <li>1. Set up 2FA using the steps above</li>
                    <li>2. Log out of your account</li>
                    <li>3. Try logging in - you'll now need your authenticator code!</li>
                    <li>4. Enter your email, password, and the 6-digit code from your app</li>
                </ol>
            </div>
        </div>
    );
};

export default TwoFactorAuth; 