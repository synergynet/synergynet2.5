package apps.jigsawapp;
// demo test
import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;  
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;

public class JigsawAppStart extends DefaultSynergyNetApp{

	ContentSystem contentSystem;

	private Level L01;
	private Level L02;
	private Level L03;
	private Level L04;
	
	private Frame image1; 
	private Frame image2; 
	private Frame image3; 
	private Frame image4;
	
	private TextLabel L01Text;
	private TextLabel L02Text;
	private TextLabel L03Text;
	private TextLabel L04Text;
	
	private ArrayList<Frame> mini_images = new ArrayList<Frame>();
	
	private TextLabel BackText;
	private TextLabel SeeOriginal;
	private int CurrentLevel;
    private boolean SeeOriginalStatus= true; 
	private Frame TempImage;
	
	public JigsawAppStart(ApplicationInfo info) {
		super(info);
	}
	
	public void SetInitialSurfaceVisibility (boolean v)
	{
		image1.setVisible(v);
		image2.setVisible(v);
		image3.setVisible(v);
		image4.setVisible(v);
		
		L01Text.setVisible(v);
		L02Text.setVisible(v);
		L03Text.setVisible(v);
		L04Text.setVisible(v);
	}
	
	public void RemoveImageChunks ()
	{
		for (int i= 0; i < mini_images.size(); ++i)
		{
			mini_images.get(i).setVisible(false);
			contentSystem.removeContentItem(mini_images.get(i));
		}
		mini_images.clear();
	}	
	
	public void PlayLevel (Level ChosenLevel)
	{
		// read the image file
        FileInputStream fis = null;
		try
		{
			fis = new FileInputStream (ChosenLevel.ImageFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
        // put the image file into an image buffer
		BufferedImage image = null;
		try
		{
			image = ImageIO.read (fis);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}  
  
		int Chunks= ChosenLevel.Columns * ChosenLevel.Rows; // determine the number of chunks
		// determines the chunk width and height  
        int chunkWidth = image.getWidth () / ChosenLevel.Columns;
        int chunkHeight = image.getHeight () / ChosenLevel.Rows;

		// create a list of buffered images that will hold the image chunks
		// these chunks are not yet written into physical files
        ArrayList<BufferedImage> imgs= new ArrayList<BufferedImage> (); 
        int count = 0;
        for (int x= 0; x < ChosenLevel.Rows; x++)
        {  
            for (int y = 0; y < ChosenLevel.Columns; y++)
            {  
            	imgs.add(new BufferedImage(chunkWidth, chunkHeight, image.getType()));
            	Graphics2D gr = imgs.get(count++).createGraphics();  
            	gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);  
            	gr.dispose();
            }  
        }  
  
		// write the image chunks into physical files (with same image type as the original big one)
        for (int i = 0; i < imgs.size(); i++)
        {  
        	try
        	{
        		File f= new File("./src/main/resources/apps/jigsawapp/Images Chunks/" + ChosenLevel.ImageFileName + "_chunk" + i + ".jpg");
        		f.deleteOnExit();
        		ImageIO.write(imgs.get(i), "JPG", f);
        	}
        	catch (IOException e)
        	{
        		e.printStackTrace();
        	}
        }  

        imgs.clear();

         for (int i=0; i < Chunks; ++i)
         {
             Frame mini_image = (Frame) contentSystem.createContentItem(Frame.class);
        	 
             mini_image.setWidth(chunkWidth / 2);
        	 mini_image.setHeight(chunkHeight / 2);
        	 mini_image.setBorderSize(0);
        	 mini_image.setScaleLimit(1, 1);
        	 mini_image.placeRandom();
        	 mini_image.rotateRandom();

        	 File input;
           	 input= new File("./src/main/resources/apps/jigsawapp/Images Chunks/" + ChosenLevel.ImageFileName + "_chunk" + i + ".jpg");
           	 try
           	 {
				mini_image.drawImage(input.toURI().toURL());
           	 }
           	 catch (MalformedURLException e)
           	 {
				e.printStackTrace();
           	 }

           	 mini_images.add(mini_image);
         }

        TempImage= null;
   		SeeOriginal = (TextLabel) contentSystem.createContentItem(TextLabel.class);
  		SeeOriginal.setText("See it!");
  		SeeOriginal.setBackgroundColour(Color.black);
  		SeeOriginal.setBorderSize(0);
  		SeeOriginal.setTextColour(Color.green);
  		SeeOriginal.setFont(new Font("serif",Font.ITALIC,24));
  		SeeOriginal.setLocation (950, 50);
  		SeeOriginal.setRotateTranslateScalable(false);
  		SeeOriginal.addItemListener(new ItemListener() {
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y,
					float pressure) {
				if (SeeOriginalStatus)
				{
					SeeOriginalStatus= false;
					SeeOriginal.setText("Hide it!");
					
					TempImage = (Frame) contentSystem.createContentItem(Frame.class);
					TempImage.setLocation(500, 350);
					TempImage.setScale(0.35f);
					TempImage.setBorderSize(10);
					TempImage.setBorderColour(Color.orange);
					TempImage.setScaleLimit(1, 1);
					TempImage.setAsTopObject();
				
					switch (CurrentLevel)
					{
					case 1:
						TempImage.setWidth(L01.ImageWidth);
						TempImage.setHeight(L01.ImageHeight);

						try {
								TempImage.drawImage(L01.ImageFile.toURI().toURL());
							}
						catch (MalformedURLException e) {e.printStackTrace();}
					break;
					case 2:
						TempImage.setWidth(L02.ImageWidth);
						TempImage.setHeight(L02.ImageHeight);

						try {
								TempImage.drawImage(L02.ImageFile.toURI().toURL());
							}
						catch (MalformedURLException e) {e.printStackTrace();}
					break;
					case 3:
						TempImage.setWidth(L03.ImageWidth);
						TempImage.setHeight(L03.ImageHeight);

						try {
								TempImage.drawImage(L03.ImageFile.toURI().toURL());
							}
						catch (MalformedURLException e) {e.printStackTrace();}
					break;
					case 4:
						TempImage.setWidth(L04.ImageWidth);
						TempImage.setHeight(L04.ImageHeight);

						try {
								TempImage.drawImage(L04.ImageFile.toURI().toURL());
							}
						catch (MalformedURLException e) {e.printStackTrace();}
					break;
					}
				}
				else
				{
					SeeOriginalStatus= true;
					SeeOriginal.setText("See it!");
					contentSystem.removeContentItem(TempImage);
				}
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
		});

   		BackText = (TextLabel) contentSystem.createContentItem (TextLabel.class);
		BackText.setText ("<Back");
		BackText.setBackgroundColour (Color.black);
		BackText.setBorderSize (0);
		BackText.setTextColour (Color.orange);
		BackText.setFont (new Font("serif",Font.ITALIC,24));
		BackText.setLocation (50, 50);
		BackText.setRotateTranslateScalable (false);
		BackText.addItemListener (new ItemListener() {
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y,
					float pressure) {
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y,
					float pressure) {
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y,
					float pressure) {
				RemoveImageChunks ();
				contentSystem.removeContentItem (BackText);
				contentSystem.removeContentItem (SeeOriginal);
				if (TempImage != null)
					contentSystem.removeContentItem (TempImage);
				SeeOriginalStatus= true;
				SetInitialSurfaceVisibility (true);
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y,
					float pressure) {
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
			}
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y,
					float pressure) {
			}
		});
	}

	public void CleanTempImages ()
	{
		String path= System.getProperty ("user.dir") + "/src/main/resources/apps/jigsawapp/Images Chunks";
		File folder = new File (path);
	    File[] listOfFiles= folder.listFiles ();

	    for (File f : listOfFiles)
	    	f.delete();
	}
	
	public void addContent()
	{
		CleanTempImages ();
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp (this);

		SynergyNetAppUtils.addTableOverlay (this); // to display blobs
		setMenuController(new HoldTopRightConfirmVisualExit(this));


		String tableID = TableIdentity.getTableIdentity ().toString ();
		
		if (!tableID.equals("red") && !tableID.equals("blue") && !tableID.equals("yellow") && !tableID.equals("green") && !tableID.equals("teacher")){
			String[] ids = {"red", "blue", "yellow", "green", "teacher"};
			tableID = ids[new Random().nextInt(4)];
		}
		
		L01= new Level (1, tableID, "img01.jpg", 2, 2);
		L02= new Level (2, tableID, "img02.jpg", 3, 3);
		L03= new Level (3, tableID, "img03.jpg", 4, 4);
		L04= new Level (4, tableID, "img04.jpg", 5, 5);
		
		image1 = (Frame) contentSystem.createContentItem (Frame.class);
        image1.setWidth (L01.ImageWidth);
        image1.setHeight (L01.ImageHeight);
        image1.setLocation (250, 550);
        image1.setScale (0.30f);
        
        
        L01Text = (TextLabel) contentSystem.createContentItem (TextLabel.class);
		L01Text.setText ("[2 x 2]");
		L01Text.setBackgroundColour (Color.black);
		L01Text.setBorderSize (0);
		L01Text.setTextColour (Color.cyan);
		L01Text.setLocation (250, 675);
		L01Text.setRotateTranslateScalable (false);
//		L01Text.setAngle (270 * FastMath.DEG_TO_RAD);
//		L01Text.setScale (3);
		
      	 try
      	 {
      		 image1.drawImage (L01.ImageFile.toURI ().toURL ());
      	 }
      	 catch (MalformedURLException e)
      	 {
			e.printStackTrace ();
      	 }
      	 
	    image2 = (Frame) contentSystem.createContentItem(Frame.class);
	    image2.setWidth(L02.ImageWidth);
	    image2.setHeight(L02.ImageHeight);
	    image2.setScale(0.30f);
	    image2.setLocation(750, 550);

        L02Text = (TextLabel) contentSystem.createContentItem(TextLabel.class);
 		L02Text.setText("[3 x 3]");
 		L02Text.setBackgroundColour(Color.black);
 		L02Text.setBorderSize(0);
 		L02Text.setTextColour(Color.yellow);
 		L02Text.setLocation (750, 675);
		L02Text.setRotateTranslateScalable(false);
// 		L02Text.setAngle(45 * FastMath.DEG_TO_RAD);
// 		L02Text.setScale(3);
         
         
       	 try
       	 {
       		 image2.drawImage(L02.ImageFile.toURI().toURL());
       	 }
       	 catch (MalformedURLException e)
       	 {
 			e.printStackTrace();
       	 }

        image3 = (Frame) contentSystem.createContentItem(Frame.class);
        image3.setWidth(L03.ImageWidth);
        image3.setHeight(L03.ImageHeight);
        image3.setScale(0.3f);
        image3.setLocation(250, 200);

        L03Text = (TextLabel) contentSystem.createContentItem(TextLabel.class);
  		L03Text.setText("[4 x 4]");
  		L03Text.setBackgroundColour(Color.black);
  		L03Text.setBorderSize(0);
  		L03Text.setTextColour(Color.orange);
  		L03Text.setLocation (250, 325);
		L03Text.setRotateTranslateScalable(false);
//  		L03Text.setAngle(45 * FastMath.DEG_TO_RAD);
//  		L03Text.setScale(3);
         
       	 try
       	 {
       		 image3.drawImage(L03.ImageFile.toURI().toURL());
       	 }
       	 catch (MalformedURLException e)
       	 {
 			e.printStackTrace();
       	 }

         image4 = (Frame) contentSystem.createContentItem(Frame.class);
         image4.setWidth(L04.ImageWidth);
         image4.setHeight(L04.ImageHeight);
         image4.setScale(0.30f);
         image4.setLocation(750, 200);

        L04Text = (TextLabel) contentSystem.createContentItem(TextLabel.class);
   		L04Text.setText("[5 x 5]");
   		L04Text.setBackgroundColour(Color.black);
   		L04Text.setBorderSize(0);
   		L04Text.setTextColour(Color.red);
   		L04Text.setLocation (750, 325);
		L04Text.setRotateTranslateScalable(false);
//   		L04Text.setAngle(45 * FastMath.DEG_TO_RAD);
//   		L04Text.setScale(3);
          
		 try
	   	 {
	   		 image4.drawImage(L04.ImageFile.toURI().toURL());
	   	 }
	   	 catch (MalformedURLException e)
	   	 {
			e.printStackTrace();
	   	 }

	   	 
        image1.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				
				SetInitialSurfaceVisibility(false);
				CurrentLevel= 1;
				PlayLevel (L01);
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
        	
        });

        image2.addItemListener(new ItemListener() {
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y,
					float pressure) {

				SetInitialSurfaceVisibility(false);
				CurrentLevel= 2;
				PlayLevel (L02);
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
		});

		image3.addItemListener(new ItemListener() {
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y,
					float pressure) {
				
				SetInitialSurfaceVisibility(false);
				CurrentLevel= 3;
				PlayLevel (L03);
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y,
					float pressure) {
				 
				
			}
		});
         
		image4.addItemListener(new ItemListener() {
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x, float y,
					float pressure) {
				 
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y,
					float pressure) {

				SetInitialSurfaceVisibility(false);
				CurrentLevel= 4;
				PlayLevel (L04);
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x, float y,
					float pressure) {
				 
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				 
			}
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x, float y,
					float pressure) {
				 
			}
		});
	}
}