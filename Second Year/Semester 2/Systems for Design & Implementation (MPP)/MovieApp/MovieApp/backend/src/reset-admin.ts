// src/reset-admin.ts
import "reflect-metadata";
import { AppDataSource } from "./data-source";
import { User } from "./entity/User";
import * as bcrypt from "bcrypt";

async function resetAdminPassword() {
    console.log("Initializing connection to database...");
    await AppDataSource.initialize();
    console.log("Database connection established");

    const userRepo = AppDataSource.getRepository(User);
    const adminEmail = "admin@gmail.com";
    const newPassword = "admin";

    // Find admin user
    const admin = await userRepo.findOneBy({ email: adminEmail });
    if (!admin) {
        console.error(`Admin user with email ${adminEmail} not found!`);
        return;
    }

    // Generate password hash manually to bypass the entity hooks
    const salt = await bcrypt.genSalt();
    const passwordHash = await bcrypt.hash(newPassword, salt);

    // Update password directly
    await userRepo.update(admin.id, { password: passwordHash });

    console.log(`Admin password reset successful for ${adminEmail}`);
    console.log("You can now login with:");
    console.log("Email: admin@gmail.com");
    console.log("Password: admin");
}

resetAdminPassword()
    .then(() => {
        console.log("✅ Password reset complete");
        process.exit(0);
    })
    .catch(error => {
        console.error("❌ Error during password reset:", error);
        process.exit(1);
    }); 