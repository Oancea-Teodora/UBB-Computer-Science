import fetch from 'node-fetch';

async function testAndreiBeforeStatus() {
    try {
        console.log('=== Testing andrei@gmail.com BEFORE enabling 2FA ===\n');

        const loginData = {
            email: 'andrei@gmail.com',
            password: 'andrei'
        };

        const response = await fetch('http://localhost:3001/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        console.log('Status:', response.status);
        const responseText = await response.text();

        try {
            const data = JSON.parse(responseText);
            if (response.status === 200) {
                console.log('✅ LOGIN SUCCESSFUL (non-2FA)');
                console.log('- User:', data.user.email);
                console.log('- 2FA Enabled:', data.user.twoFactorEnabled);
                console.log('- Token received:', !!data.token);
            } else {
                console.log('❌ LOGIN FAILED:', data.error);
            }
        } catch (e) {
            console.log('Response:', responseText);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

testAndreiBeforeStatus(); 