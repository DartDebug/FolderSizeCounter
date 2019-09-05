import java.io.File;
import java.nio.file.*;

public class FolderSizeCounter
{
    private static long size = 0;
    public static void main(String[] args)
    {
        String path = "";
        String pathSource = "";
        String pathTarget = "";

        System.out.printf("Размер папки: %,d байт\n", sizeCountForAllFolders(path));
        copyFolder(pathSource, pathTarget);
    }

    private static long sizeCountForAllFolders(String path)
    {
        long localSize = 0;
        try
        {
            Files.list(Path.of(path)).forEach(file ->
            {
                try
                {
//                    Расскомментировать для подсчета всех вложеных каталогов
//                    if (Files.isDirectory(Path.of(file.toString()))) size += sizeCountForAllFolders(file.toString());
                    size += Files.size(Path.of(file.toString()));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            });
            localSize = size;
            size = 0;
            return localSize;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return localSize;
    }

    private static void copyFolder(String sourceFolderName, String targetFolderName)
    {
        try
        {
            File folder = new File(sourceFolderName);
            File[] filesList = folder.listFiles();
            Path destFolder = Paths.get(targetFolderName);
            if (filesList != null)
            {
                for (var file : filesList)
                    Files.copy(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                for (var file : filesList)
                    if (file.isDirectory())
                        copyFolder(sourceFolderName + "\\" + file.getName(),
                                targetFolderName + "\\" + file.getName());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
