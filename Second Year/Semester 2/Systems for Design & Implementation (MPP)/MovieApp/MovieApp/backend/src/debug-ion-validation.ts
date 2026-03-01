import { AppDataSource } from './data-source';
import { User } from './entity/User';
import bcrypt from 'bcrypt';

async function debugIonValidation() {
    try {
        await AppDataSource.initialize();

        const email = 'ion@gmail.com';
        const password = 'ionion';

        console.log('=== Testing ion@gmail.com Authentication Logic ===\n');

        // Step 1: Find user using repository (like auth route does)
        console.log('1. Finding user using TypeORM repository...');
        const userRepo = AppDataSource.getRepository(User);
        const user = await userRepo.findOne({ where: { email } });

        if (!user) {
            console.log('❌ User not found via repository');
            return;
        }

        console.log('✅ User found via repository:');
        console.log('   ID:', user.id);
        console.log('   Email:', user.email);
        console.log('   Name:', user.name);
        console.log('   2FA Enabled:', user.twoFactorEnabled);
        console.log('   Password hash:', user.password.substring(0, 30) + '...');

        // Step 2: Test password validation using entity method
        console.log('\n2. Testing password validation using entity method...');
        const isValidEntity = await user.validatePassword(password);
        console.log('   Entity validatePassword():', isValidEntity ? '✅ VALID' : '❌ INVALID');

        // Step 3: Test password validation using direct bcrypt
        console.log('\n3. Testing password validation using direct bcrypt...');
        const isValidBcrypt = await bcrypt.compare(password, user.password);
        console.log('   Direct bcrypt.compare():', isValidBcrypt ? '✅ VALID' : '❌ INVALID');

        // Step 4: Show what should happen in auth route
        console.log('\n4. Expected auth route behavior:');
        if (isValidEntity) {
            if (user.twoFactorEnabled) {
                console.log('   ✅ Password valid + 2FA enabled = Should return 401 with requires2FA: true');
            } else {
                console.log('   ✅ Password valid + 2FA disabled = Should return 200 with tokens');
            }
        } else {
            console.log('   ❌ Password invalid = Should return 401 with "Invalid credentials"');
        }

        // Step 5: Check if there's a discrepancy
        console.log('\n5. Analysis:');
        if (isValidEntity !== isValidBcrypt) {
            console.log('   🚨 DISCREPANCY: Entity method and bcrypt give different results!');
        } else if (!isValidEntity) {
            console.log('   🚨 PROBLEM: Password is invalid - needs to be fixed');
        } else {
            console.log('   ✅ Password validation is working correctly');
        }

        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

debugIonValidation(); 