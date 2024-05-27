package com.server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileWriterTest {

	private static final String TEST_FOLDER = "testFolder";
	private static final String TEST_FILE = "testFolder/testFile.txt";

	@BeforeEach
	public void setUp() {
		// Ensure the test folder is clean before each test
		deleteFolder(new File(TEST_FOLDER));
	}

	@AfterEach
	public void tearDown() {
		// Clean up after each test
		deleteFolder(new File(TEST_FOLDER));
	}

	@Test
	public void testEnsureFolder() {
		assertTrue(fileWriter.ensureFolder(TEST_FOLDER));
		assertTrue(new File(TEST_FOLDER).exists());
	}

	@Test
	public void testEnsureFolderForFile() {
		assertTrue(fileWriter.ensureFolderForFile(TEST_FILE));
		assertTrue(new File(TEST_FOLDER).exists());
	}

	@Test
	public void testEnsureFile() {
		assertTrue(fileWriter.ensureFile(TEST_FILE));
		assertTrue(new File(TEST_FILE).exists());
	}

	@Test
	public void testWrite() throws IOException {
		String content = "Hello, World!";
		fileWriter.write(TEST_FILE, content);
		assertEquals(content, readFile(TEST_FILE));
	}

	@Test
	public void testWriteWithEncoding() throws IOException {
		String content = "Hello, World!";
		fileWriter.writeWithEncoding(TEST_FILE, content, "UTF-8");
		assertEquals(content, readFile(TEST_FILE));
	}

	@Test
	public void testAppendToFile() throws IOException {
		String content1 = "Hello, ";
		String content2 = "World!";
		fileWriter.write(TEST_FILE, content1);
		fileWriter.appendToFile(TEST_FILE, content2);
		assertEquals(content1 + content2, readFile(TEST_FILE));
	}

	@Test
	public void testGetFolderPath() {
		String filePath = "some/folder/testFile.txt";
		assertEquals("some/folder", fileWriter.getFolderPath(filePath));
	}

	private void deleteFolder(File folder) {
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFolder(f);
					} else {
						f.delete();
					}
				}
			}
			folder.delete();
		}
	}

	private String readFile(String path) throws IOException {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		}
		return content.toString();
	}
}
