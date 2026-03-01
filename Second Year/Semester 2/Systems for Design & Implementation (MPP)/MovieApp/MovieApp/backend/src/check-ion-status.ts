import { AppDataSource } from './data-source';

async function checkIonStatus() {
    try {
        await AppDataSource.initialize();

        const user = await AppDataSource.query(
            'SELECT id, email, name, role, twoFactorEnabled, twoFactorSecret FROM user WHERE email = ?',
            ['ion@gmail.com']
        );

        if (user.length === 0) {
            console.log('❌ User ion@gmail.com not found');
            return;
        }

        const ionUser = user[0];
        console.log('=== ion@gmail.com User Status ===');
        console.log('ID:', ionUser.id);
        console.log('Email:', ionUser.email);
        console.log('Name:', ionUser.name);
        console.log('Role:', ionUser.role);
        console.log('2FA Enabled:', ionUser.twoFactorEnabled);
        console.log('Has 2FA Secret:', !!ionUser.twoFactorSecret);

        if (ionUser.twoFactorEnabled) {
            console.log('\n✅ 2FA is ENABLED for this user');
            console.log('This user will need to provide a 2FA code during login');
        } else {
            console.log('\n❌ 2FA is DISABLED for this user');
            console.log('This user can login with just email/password');
        }

        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

checkIonStatus(); 