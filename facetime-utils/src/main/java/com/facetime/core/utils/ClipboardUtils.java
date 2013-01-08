/*
 * Copyright (C) 2010 SUNRico Inc.
 * ------------------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Visit  http://www.streets.com for more infomation.
 *
 * ----------------------------------------------------------------------------------
 */

package com.facetime.core.utils;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Clipboard utilities.
 */
public class ClipboardUtils {

	/**
	 * Copies string to system clipboard.
	 */
	public static void copyToClipboard(String str) {
		StringSelection copyItem = new StringSelection(str);
		Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(copyItem, null);
	}

	/**
	 * Reads a string from system clipboard.
	 */
	public static String getStringFromClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable paste = clipboard.getContents(null);
		if (paste == null)
			return null;
		try {
			return (String) paste.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception ex) {
			return null;
		}
	}

}
