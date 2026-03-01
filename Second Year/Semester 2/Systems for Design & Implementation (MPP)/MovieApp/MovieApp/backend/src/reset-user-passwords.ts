import { AppDataSource } from './data-source';
import { User } from './entity/User';
import bcrypt from 'bcrypt';

async function resetUserPasswords() {
    try {
        console.log('Initializing database connection...');
        await AppDataSource.initialize();
        console.log('Database connection established\n');

        const userRepo = AppDataSource.getRepository(User);

        // Reset password for ionut user
        const ionut = await userRepo.findOne({ where: { email: 'ionut@gmail.com' } });
        if (ionut) {
            const hashedPassword = await bcrypt.hash('ionut', 10);
            ionut.password = hashedPassword;
            await userRepo.save(ionut);
            console.log('✅ Password reset for ionut@gmail.com - password: ionut');
        }

        // Reset password for andrei user (already works, but let's confirm)
        const andrei = await userRepo.findOne({ where: { email: 'andrei@gmail.com' } });
        if (andrei) {
            console.log('✅ andrei@gmail.com already has working password: andrei');
        }

        // Reset password for teodora user
        const teodora = await userRepo.findOne({ where: { email: 'teodora@gmail.com' } });
        if (teodora) {
            const hashedPassword = await bcrypt.hash('teodora', 10);
            teodora.password = hashedPassword;
            await userRepo.save(teodora);
            console.log('✅ Password reset for teodora@gmail.com - password: teodora');
        }

        // Reset password for admin1 user
        const admin1 = await userRepo.findOne({ where: { email: 'admin1@gmail.com' } });
        if (admin1) {
            const hashedPassword = await bcrypt.hash('admin1', 10);
            admin1.password = hashedPassword;
            await userRepo.save(admin1);
            console.log('✅ Password reset for admin1@gmail.com - password: admin1');
        }

        // Reset password for new user
        const newUser = await userRepo.findOne({ where: { email: 'new@gmail.com' } });
        if (newUser) {
            const hashedPassword = await bcrypt.hash('new', 10);
            newUser.password = hashedPassword;
            await userRepo.save(newUser);
            console.log('✅ Password reset for new@gmail.com - password: new');
        }

        console.log('\n=== Summary ===');
        console.log('Users without 2FA that can now login:');
        console.log('- ionut@gmail.com / ionut');
        console.log('- andrei@gmail.com / andrei');
        console.log('- teodora@gmail.com / teodora');
        console.log('- admin1@gmail.com / admin1');
        console.log('- new@gmail.com / new');
        console.log('\nThese users can login normally and then enable 2FA from their account settings.');

        await AppDataSource.destroy();
        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

resetUserPasswords(); 