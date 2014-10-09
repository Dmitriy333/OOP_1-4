package by.bsuir.filefilter;

import java.io.File;
import java.io.FileFilter;

public class Filter implements FileFilter {
    public boolean accept(File pathname) 
    {
        // �������� ��� ��� ���� � ��� �� ������������� �� .txt 
       return pathname.isFile() && pathname.getName().endsWith(".txt");
    }
}