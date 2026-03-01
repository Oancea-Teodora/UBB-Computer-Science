import fetch from 'node-fetch';

async function testIonLogin() {
    try {
        console.log('Testing ion@gmail.com login via API...\n');

        const loginData = {
            email: 'ion@gmail.com',
            password: 'ionion'
        };

        const response = await fetch('http://localhost:3001/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        console.log('Status:', response.status);
        console.log('Status Text:', response.statusText);

        const responseText = await response.text();
        console.log('Response:', responseText);

        try {
            const data = JSON.parse(responseText);
            console.log('\nParsed Response:');
            console.log('- User ID:', data.user?.id);
            console.log('- Email:', data.user?.email);
            console.log('- Name:', data.user?.name);
            console.log('- Role:', data.user?.role);
            console.log('- 2FA Enabled:', data.user?.twoFactorEnabled);
            console.log('- Token received:', !!data.token);
            console.log('- Access Token received:', !!data.accessToken);
            console.log('- Refresh Token received:', !!data.refreshToken);

            if (response.status === 200) {
                console.log('\n✅ LOGIN SUCCESSFUL! User can login normally without 2FA.');
            } else {
                console.log('\n❌ LOGIN FAILED');
            }
        } catch (e) {
            console.log('Could not parse response as JSON');
        }

    } catch (error) {
        console.error('Error testing API:', error);
    }
}

testIonLogin(); 