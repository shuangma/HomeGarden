package com.hummingbee.garden.common;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ViewHelper {
	public static JLabel createImageLabel(int width, int height, String imagePath){
		JLabel imageLabel = new JLabel();
		File imageFile = new File(imagePath);
		if(!imageFile.exists())
			return imageLabel;
		ImageIcon imageIcon =  getResizeImageIcon(width, height, imagePath);
		imageLabel.setIcon(imageIcon);
		return imageLabel;
	}
	
	public static ImageIcon getResizeImageIcon(int width, int height, String imagePath) {
		ImageIcon oriImageIcon = new ImageIcon(imagePath);
		Image oriImage = oriImageIcon.getImage();
		Image resizedImage = oriImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
		return resizedImageIcon;
	}
}
