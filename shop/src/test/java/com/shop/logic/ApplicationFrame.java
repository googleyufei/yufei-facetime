package com.shop.logic;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ApplicationFrame extends Frame {
	/** */
	private static final long serialVersionUID = 1L;

	public ApplicationFrame() {
		this("ApplicationFrame v1.0");
	}

	public ApplicationFrame(String title) {
		super(title);
		createUI();
	}

	public void center() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		int x = (screenSize.width - frameSize.width) / 2;
		int y = (screenSize.height - frameSize.height) / 2;
		setLocation(x, y);
	}

	protected void createUI() {
		setSize(500, 400);
		center();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}
}