package com.dawid.library.io.file;

import com.dawid.library.model.Library;

public interface FileManager {
    Library importData();
    void exportData(Library library);
}
