package com.socialmeadia.socialmedia.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    @Value("${user.avatar.storage.path}")
    private String baseDestination;

    public void storeAvatar(String username, String sourcePath, String avatarName) throws IOException {
        Path userFolder = Paths.get(baseDestination, username);
        if (!Files.exists(userFolder)) {
            Files.createDirectories(userFolder);
        }

        Path sourceFile = Paths.get(sourcePath, avatarName);
        if (!Files.exists(sourceFile)) {
            throw new IOException("Avatar file does not exist at: " + sourceFile.toString());
        }

        Path destinationFile = userFolder.resolve(avatarName);
        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
