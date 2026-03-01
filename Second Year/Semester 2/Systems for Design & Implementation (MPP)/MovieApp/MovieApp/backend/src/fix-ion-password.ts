import { AppDataSource } from './data-source';
import bcrypt from 'bcrypt';

async function fixIonPassword() {
    try {
        await AppDataSource.initialize();

        const email = 'ion@gmail.com';
        const password = 'ionion';

        console.log(`Checking if user ${email} exists...`);

        // Check if user exists
        const existingUser = await AppDataSource.query(
            'SELECT id, email, name FROM user WHERE email = ?',
            [email]
        );

        if (existingUser.length === 0) {
            console.log(`❌ User ${email} not found in database`);
            console.log('Available users:');
            const allUsers = await AppDataSource.query('SELECT email FROM user');
            allUsers.forEach((user: any) => console.log(`- ${user.email}`));
            return;
        }

        console.log(`✅ User found: ${existingUser[0].name} (${existingUser[0].email})`);

        console.log(`Hashing password "${password}"...`);
        const hashedPassword = await bcrypt.hash(password, 10);

        console.log('Updating password in database...');
        await AppDataSource.query(
            'UPDATE user SET password = ?, updatedAt = CURRENT_TIMESTAMP WHERE email = ?',
            [hashedPassword, email]
        );

        console.log(`✅ Password updated for ${email}`);

        // Test the password
        console.log('Testing login...');
        const updatedUser = await AppDataSource.query(
            'SELECT password FROM user WHERE email = ?',
            [email]
        );

        const isValid = await bcrypt.compare(password, updatedUser[0].password);
        console.log(`Password "${password}" validation: ${isValid ? '✅ SUCCESS' : '❌ FAILED'}`);

        if (isValid) {
            console.log(`\n🎉 Login credentials for ${email}:`);
            console.log(`Email: ${email}`);
            console.log(`Password: ${password}`);
            console.log('User can now login normally!');
        }

        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

fixIonPassword(); 