/*
 * SevenZipDecompressor.java
 *
 * Copyright © 2018-2019 N00byKing <N00byKing@hotmail.de>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com> and Raymond Lai <airwave209gt at gmail.com>.
 *
 * This file is part of AmazeFileManager.
 *
 * AmazeFileManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AmazeFileManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AmazeFileManager. If not, see <http ://www.gnu.org/licenses/>.
 */

package com.amaze.filemanagerZichongTest.filesystem.compressed.showcontents.helpers;

import android.content.Context;
import androidx.annotation.NonNull;

import com.amaze.filemanagerZichongTest.adapters.data.CompressedObjectParcelable;
import com.amaze.filemanagerZichongTest.asynchronous.asynctasks.AsyncTaskResult;
import com.amaze.filemanagerZichongTest.asynchronous.asynctasks.compress.SevenZipHelperTask;
import com.amaze.filemanagerZichongTest.filesystem.compressed.showcontents.Decompressor;
import com.amaze.filemanagerZichongTest.utils.OnAsyncTaskFinished;

import java.util.ArrayList;

public class SevenZipDecompressor extends Decompressor {

    public SevenZipDecompressor(@NonNull Context context) {
        super(context);
    }

    @Override
    public SevenZipHelperTask changePath(String path, boolean addGoBackItem,
                                       OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new SevenZipHelperTask(filePath, path, addGoBackItem, onFinish);
    }
}
