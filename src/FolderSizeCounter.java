import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderSizeCounter
{
    private static long size = 0;
    public static void main(String[] args)
    {
        String path = "";
        String pathSource = "";
        String pathDestination = "";
//        copyFolder(pathSource, pathDestination);
        formatPrintSize(sizeCountWithWalk(path));
        copyFolderWithWalk(pathSource, pathDestination);
    }

    private static void formatPrintSize(long size)
    {
        int count = 0;
        String[] prefixSize = {"b", "Kb", "Mb", "Gb", "Tb"};
        while(size / 1024 != 0)
        {
            size = size / 1024;
            count++;
        }
        System.out.printf("Размер папки: %,d %s\n", size, prefixSize[count]);
    }

    private static  long sizeCountWithWalk(String path)
    {
        size = 0;
        try
        {
            return Files.walk(Path.of(path)).map(Path::toFile).filter(File::isFile)
                    .mapToLong(File::length).reduce(0, Long::sum);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return size;
    }

//        private static void copyFolder(String sourceFolderName, String targetFolderName)
//    {
//        try
//        {
//            File folder = new File(sourceFolderName);
//            File[] filesList = folder.listFiles();
//            Path destFolder = Paths.get(targetFolderName);
//            if (filesList != null)
//            {
//                for (var file : filesList)
//                    Files.copy(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
//                for (var file : filesList)
//                    if (file.isDirectory())
//                        copyFolder(sourceFolderName + "\\" + file.getName(),
//                                targetFolderName + "\\" + file.getName());
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    private static void copyFolderWithWalk(String sourceFolderName, String targetFolderName)
    {
        Path source = Paths.get(sourceFolderName);
        Path destination = Paths.get(targetFolderName);
        try
        {
            Files.walkFileTree(source, new CopyVisitor(source, destination));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private static class CopyVisitor extends SimpleFileVisitor
    {
        Path source, destination;
        CopyVisitor(Path sourceFolder, Path destinationFolder)
        {
            this.source = sourceFolder;
            this.destination = destinationFolder;
        }
        public FileVisitResult visitFile(Object path, BasicFileAttributes attrs) throws IOException {
            Path newd = destination.resolve(source.relativize((Path)path));
            Files.copy((Path)path, newd, StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }

        public FileVisitResult preVisitDirectory(Object path, BasicFileAttributes fileAttributes) throws IOException
        {
            Path newd = destination.resolve(source.relativize((Path)path));
            Files.copy((Path)path, newd, StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    }
}
