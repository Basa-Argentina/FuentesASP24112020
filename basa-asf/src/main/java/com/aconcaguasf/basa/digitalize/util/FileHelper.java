package com.aconcaguasf.basa.digitalize.util;
/*
 *
 *  Copyright (c) 2017./ Aconcagua SF S.A.
 *  *
 *  Licensed under the Goycoolea inc License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://crossover.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author 		: Hector Goycoolea
 *  @developer		: Hector Goycoolea
 *
 *  Date Changes
 *  02/09/17 03:53 Argentina Timezone
 *
 *  Notes
 *
 *  The Email helper uses basic java mail protocols and we are not using
 *  bean for configuration so we avoid the @Service("name") on the servlet-context
 *  this is a totally diff structure and this way we can manage a bit more the memmory
 *  of the iterations loop for sending massive mails.
 */
import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.RemitosDetalle;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileHelper {
    /**
     *
     */


    public static void getFileContent( List<Object> listaElementos, ArrayList<ArrayList> listaElementos2, String path1, String path2) {

        /*   List<Path> filePaths = null; // Step 1: get all files from a directory
           filePaths = filePathsList(path1);
           List<Path> filteredFilePaths = filter(filePaths); // Step 2: filter by ".txt"
           String[] contentOfFiles = getContentOfFiles(filteredFilePaths).values().toString().trim().split(", |\\[|]|\t"); // Step 3: get content of files
           List<String> memo1 = Arrays.asList(contentOfFiles);
           //move(filteredFilePaths, path1); // Step 4: move files to destination
           filePaths = filePathsList(path2);
           List<Path> filteredFilePaths2 = filter(filePaths); // Step 2: filter by ".txt"
           String[] contentOfFiles2= getContentOfFiles(filteredFilePaths2).values().toString().trim().split(", |\\[|]");
           List<String> memo2 = Arrays.asList(contentOfFiles2);
           //move(filteredFilePaths, path2); // Step 4: move files to destination
*/
        for (Object a :listaElementos)
            //List<Long> l = a;

        System.console();
        }


    public static List<Path> filePathsList(String directory) throws IOException {
        List<Path> filePaths = new ArrayList<>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(FileSystems.getDefault().getPath(directory));
        for (Path path : directoryStream) {
            filePaths.add(path);
        }
        return filePaths;
    }

    private static List<Path> filter(List<Path> filePaths) {
        List<Path> filteredFilePaths = new ArrayList<>();
        for (Path filePath : filePaths) {
            if (filePath.getFileName().toString().endsWith(".txt")) {
                filteredFilePaths.add(filePath);
            }
        }
        return filteredFilePaths;
    }

    private static Map<Path, List<String>> getContentOfFiles(List<Path> filePaths) throws IOException {
        Map<Path, List<String>> contentOfFiles = new HashMap<>();
        for (Path filePath : filePaths) {
            contentOfFiles.put(filePath, new ArrayList<>());
            Files.readAllLines(filePath).forEach(contentOfFiles.get(filePath)::add);
        }
        return contentOfFiles;
    }

    private static void move(List<Path> filePaths, String target) throws IOException {
        Path targetDir = FileSystems.getDefault().getPath(target);
        if (!Files.isDirectory(targetDir)) {
            targetDir = Files.createDirectories(Paths.get(target));
        }
        for (Path filePath : filePaths) {
            System.out.println("Moving " + filePath.getFileName() + " to " + targetDir.toAbsolutePath());
            Files.move(filePath, Paths.get(target, filePath.getFileName().toString().substring(0,filePath.getFileName().toString().length()-4)+"_ok.txt"), StandardCopyOption.ATOMIC_MOVE);
        }
    }
}
