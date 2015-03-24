package apps.mysteriestableportal;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;

/**
 * The Class DinnerDisasterBuilder.
 */
public class DinnerDisasterBuilder {

	/** The back color. */
	Color backColor = Color.white;

	/** The border color. */
	Color borderColor = Color.gray;

	/** The border size. */
	int borderSize = 2;

	/** The font. */
	Font font = new Font("Arial", Font.PLAIN, 16);

	/** The random generator. */
	Random randomGenerator = new Random();

	/** The text color. */
	Color textColor = Color.black;

	/**
	 * Instantiates a new dinner disaster builder.
	 */
	public DinnerDisasterBuilder() {

	}

	/**
	 * Builds the.
	 *
	 * @param contentSystem
	 *            the content system
	 * @return the list
	 */
	public List<ContentItem> build(ContentSystem contentSystem) {

		List<ContentItem> items = new ArrayList<ContentItem>();

		MultiLineTextLabel l1 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l1.setId("IDD1");
		l1.setCRLFSeparatedString("“Yuck!” cried Ruby, making \n a face at the slice of pizza \n in front of her. “I can’t stand \n pepperoni!”");
		l1.setBackgroundColour(backColor);
		l1.setTextColour(textColor);
		l1.setBorderColour(borderColor);
		l1.setBorderSize(2);
		l1.setFont(font);
		l1.centerItem();
		l1.rotateRandom();
		l1.setOrder(randomGenerator.nextInt(100));
		l1.setNote("stage1");
		items.add(l1);

		MultiLineTextLabel l2 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l2.setId("IDD2");
		l2.setCRLFSeparatedString("“Hey, anybody want these \n chicken wings?” asked Grace. \n “I don’t like anything with meat in it.”");
		l2.setBackgroundColour(backColor);
		l2.setTextColour(textColor);
		l2.setBorderColour(borderColor);
		l2.setBorderSize(2);
		l2.setFont(font);
		l2.centerItem();
		l2.rotateRandom();
		l2.setOrder(randomGenerator.nextInt(100));
		l2.setNote("stage1");
		items.add(l2);
		
		MultiLineTextLabel l3 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l3.setId("IDD3");
		l3.setCRLFSeparatedString("“Well yogurt is the only \n thing I like on the menu” replied \n Tanya “And there’s no way I’m going \n to eat THIS!” At that she poked her salad \n with a fork.");
		l3.setBackgroundColour(backColor);
		l3.setTextColour(textColor);
		l3.setBorderColour(borderColor);
		l3.setBorderSize(2);
		l3.setFont(font);
		l3.centerItem();
		l3.rotateRandom();
		l3.setOrder(randomGenerator.nextInt(100));
		l3.setNote("stage1");
		items.add(l3);
		
		MultiLineTextLabel l4 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l4.setId("IDD4");
		l4.setCRLFSeparatedString("The new cook at school, \n Mrs Baker, has mixed up \n the trays with the children’s \n school dinners on.");
		l4.setBackgroundColour(backColor);
		l4.setTextColour(textColor);
		l4.setBorderColour(borderColor);
		l4.setBorderSize(2);
		l4.setFont(font);
		l4.centerItem();
		l4.rotateRandom();
		l4.setOrder(randomGenerator.nextInt(100));
		l4.setNote("stage1");
		items.add(l4);
		
		MultiLineTextLabel l5 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l5.setId("IDD5");
		l5.setCRLFSeparatedString("Mike scooped up a spoonful of \n his yogurt and grumbled, \n “Everybody knows I’m allergic to \n this stuff.”");
		l5.setBackgroundColour(backColor);
		l5.setTextColour(textColor);
		l5.setBorderColour(borderColor);
		l5.setBorderSize(2);
		l5.setFont(font);
		l5.centerItem();
		l5.rotateRandom();
		l5.setOrder(randomGenerator.nextInt(100));
		l5.setNote("stage1");
		items.add(l5);
		
		MultiLineTextLabel l6 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l6.setId("IDD6");
		l6.setCRLFSeparatedString("“Don’t look at me,” moaned \n Jack. “I hate any food with cheese \n on it.” At that he pushed away \n his cheeseburger.");
		l6.setBackgroundColour(backColor);
		l6.setTextColour(textColor);
		l6.setBorderColour(borderColor);
		l6.setBorderSize(2);
		l6.setFont(font);
		l6.centerItem();
		l6.rotateRandom();
		l6.setOrder(randomGenerator.nextInt(100));
		l6.setNote("stage1");
		items.add(l6);
		
		Frame frame7 = (Frame) contentSystem.createContentItem(Frame.class);
		frame7.setId("IDD7");
		frame7.drawImage(MysteriesPortalClientApp.class.getResource("jack.png"));
		frame7.centerItem();
		frame7.setBorderColour(borderColor);
		frame7.setBorderSize(2);
		frame7.rotateRandom();
		frame7.setOrder(randomGenerator.nextInt(100));
		frame7.setNote("stage2");
		items.add(frame7);
		
		Frame frame8 = (Frame) contentSystem.createContentItem(Frame.class);
		frame8.setId("IDD8");
		frame8.drawImage(MysteriesPortalClientApp.class
				.getResource("tanya.png"));
		frame8.centerItem();
		frame8.setBorderColour(borderColor);
		frame8.setBorderSize(2);
		frame8.rotateRandom();
		frame8.setOrder(randomGenerator.nextInt(100));
		frame8.setNote("stage2");
		items.add(frame8);
		
		Frame frame9 = (Frame) contentSystem.createContentItem(Frame.class);
		frame9.setId("IDD9");
		frame9.drawImage(MysteriesPortalClientApp.class
				.getResource("grace.png"));
		frame9.centerItem();
		frame9.setBorderColour(borderColor);
		frame9.setBorderSize(2);
		frame9.rotateRandom();
		frame9.setOrder(randomGenerator.nextInt(100));
		frame9.setNote("stage2");
		items.add(frame9);
		
		Frame frame10 = (Frame) contentSystem.createContentItem(Frame.class);
		frame10.setId("IDD10");
		frame10.drawImage(MysteriesPortalClientApp.class
				.getResource("mike.png"));
		frame10.centerItem();
		frame10.setBorderColour(borderColor);
		frame10.setBorderSize(2);
		frame10.rotateRandom();
		frame10.setOrder(randomGenerator.nextInt(100));
		frame10.setNote("stage2");
		items.add(frame10);
		
		Frame frame11 = (Frame) contentSystem.createContentItem(Frame.class);
		frame11.setId("IDD11");
		frame11.drawImage(MysteriesPortalClientApp.class
				.getResource("ruby.png"));
		frame11.centerItem();
		frame11.setBorderColour(borderColor);
		frame11.setBorderSize(2);
		frame11.rotateRandom();
		frame11.setOrder(randomGenerator.nextInt(100));
		frame11.setNote("stage2");
		items.add(frame11);
		
		Frame frame13 = (Frame) contentSystem.createContentItem(Frame.class);
		frame13.setId("IDD13");
		frame13.drawImage(MysteriesPortalClientApp.class
				.getResource("burger.png"));
		frame13.centerItem();
		frame13.setBorderColour(borderColor);
		frame13.setBorderSize(2);
		frame13.rotateRandom();
		frame13.setOrder(randomGenerator.nextInt(100));
		frame13.setNote("stage2");
		items.add(frame13);
		
		Frame frame14 = (Frame) contentSystem.createContentItem(Frame.class);
		frame14.setId("IDD14");
		frame14.drawImage(MysteriesPortalClientApp.class
				.getResource("fries.png"));
		frame14.centerItem();
		frame14.setBorderColour(borderColor);
		frame14.setBorderSize(2);
		frame14.rotateRandom();
		frame14.setOrder(randomGenerator.nextInt(100));
		frame14.setNote("stage2");
		items.add(frame14);
		
		Frame frame15 = (Frame) contentSystem.createContentItem(Frame.class);
		frame15.setId("IDD15");
		frame15.drawImage(MysteriesPortalClientApp.class
				.getResource("salad.png"));
		frame15.centerItem();
		frame15.setBorderColour(borderColor);
		frame15.setBorderSize(2);
		frame15.rotateRandom();
		frame15.setOrder(randomGenerator.nextInt(100));
		frame15.setNote("stage2");
		items.add(frame15);
		
		Frame frame16 = (Frame) contentSystem.createContentItem(Frame.class);
		frame16.setId("IDD16");
		frame16.drawImage(MysteriesPortalClientApp.class
				.getResource("yogurt.png"));
		frame16.centerItem();
		frame16.setBorderColour(borderColor);
		frame16.setBorderSize(2);
		frame16.rotateRandom();
		frame16.setOrder(randomGenerator.nextInt(100));
		frame16.setNote("stage2");
		items.add(frame16);
		
		Frame frame17 = (Frame) contentSystem.createContentItem(Frame.class);
		frame17.setId("IDD17");
		frame17.drawImage(MysteriesPortalClientApp.class
				.getResource("pizza.png"));
		frame17.centerItem();
		frame17.setBorderColour(borderColor);
		frame17.setBorderSize(2);
		frame17.rotateRandom();
		frame17.setOrder(randomGenerator.nextInt(100));
		frame17.setNote("stage2");
		items.add(frame17);
		
		MultiLineTextLabel l12 = (MultiLineTextLabel) contentSystem
				.createContentItem(MultiLineTextLabel.class);
		l12.setId("IDDTitle");
		l12.setCRLFSeparatedString("Can you work out what Mike should have to eat? ");
		l12.setLocalLocation(517, 720);
		l12.setOrder(99999);
		l12.setBackgroundColour(Color.black);
		l12.setTextColour(Color.green);
		l12.setBorderColour(Color.green);
		l12.setBorderSize(2);
		l12.setFont(new Font("Arial", Font.PLAIN, 30));
		l12.setRotateTranslateScalable(false);
		l12.setBringToTopable(false);
		l12.setNote("stage1");
		items.add(l12);
		return items;
	}
}
