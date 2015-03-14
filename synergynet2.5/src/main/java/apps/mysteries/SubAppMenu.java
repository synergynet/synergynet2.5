/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mysteries;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SubAppMenuEventListener;

public class SubAppMenu {
	/*
	public final static String SUB_APP_LONDONFIRE = "LONDON FIRE";
	public final static String SUB_APP_TIPPINGWAITRESSES = "TIPPING WAITRESSES";
	public final static String SUB_APP_ROBERTDIXON = "ROBERT DIXON";
	public final static String SUB_APP_DINNERDISASTER = "DINNER DISASTER";
	public final static String SUB_APP_WALTZER = "WALTZER";
	public final static String SUB_APP_SNEAKYSYDNEY = "SNEAKY SYDNEY";
	public final static String SUB_APP_SCHOOLTRIP = "SCHOOL TRIP";
	*/
	
	/*
	public final static String SUB_APP_JUDITH_AND_CAT = "JUDITH AND CAT";
	public final static String SUB_APP_MONEY_TRAIL = "MONEY TRAIL";
	public final static String SUB_APP_SCHOOL_TRIP = "SCHOOL TRIP";
	public final static String SUB_APP_TRIPLETS = "TRIPLETS";
	public final static String SUB_APP_WALTZER = "WALTZER";
	public final static String SUB_APP_SNEAKYSYDNEY = "SNEAKY SYDNEY";
	*/
	/*
	public final static String SUB_APP_ARCHIE = "MARY AND ARCHIE";
	public final static String SUB_APP_DAN_THE_DEALER = "DAN THE DEALER";
	public final static String SUB_APP_FORGETFUL_CHARLIE = "FORGETFUL CHARLIE";
	public final static String SUB_APP_MEET_UP = "LUNCH MEETING";
	public final static String SUB_APP_SWIM = "SWIMMING RACE";
	*/
	
	public final static String SUB_APP_DINNERDISASTER = "DINNER DISASTER";
	public final static String SUB_APP_VLE = "VLE";
	public final static String SUB_APP_SNEAKYSYDNEY = "SNEAKY SYDNEY";
	public final static String SUB_APP_SCHOOL_TRIP = "SCHOOL TRIP";
	public final static String SUB_APP_WILF = "WILF";
	public final static String SUB_APP_LOGIC = "Sports Day";



	protected ContentSystem contentSystem;
	protected List<SubAppMenuEventListener> subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
	
	public SubAppMenu(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	public ListContainer getSubAppMenu(){

		final ListContainer menu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
//		menu.setSuperSteadfast(true);
		menu.setWidth(200);
		menu.setItemHeight(30);
		menu.getBackgroundFrame().setBackgroundColour(Color.gray);	
		
		
		SimpleButton dinnerDisaster = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		dinnerDisaster.setAutoFitSize(false);
		dinnerDisaster.setText(SUB_APP_DINNERDISASTER);
		dinnerDisaster.setBackgroundColour(Color.lightGray);
		dinnerDisaster.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/dinnerdisaster2/dinnerdisaster2.xml", SUB_APP_DINNERDISASTER);
				menu.setVisible(false);
			}			
		});
		
		SimpleButton VLE = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		VLE.setAutoFitSize(false);
		VLE.setText(SUB_APP_VLE);
		VLE.setBackgroundColour(Color.lightGray);
		VLE.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/VLE/vle.xml", SUB_APP_VLE);
				menu.setVisible(false);
			}			
		});
		
		SimpleButton schoolTripButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		schoolTripButton.setAutoFitSize(false);
		schoolTripButton.setText(SUB_APP_SCHOOL_TRIP);
		schoolTripButton.setBackgroundColour(Color.lightGray);
		schoolTripButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/school_trip_y5/school_trip_y5.xml", SUB_APP_SCHOOL_TRIP);
				menu.setVisible(false);				
			}			
		});
		
		SimpleButton sneakyButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		sneakyButton.setAutoFitSize(false);
		sneakyButton.setText(SUB_APP_SNEAKYSYDNEY);
		sneakyButton.setBackgroundColour(Color.lightGray);
		sneakyButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/sneakysydney/sneakysydney.xml", SUB_APP_SNEAKYSYDNEY);
				menu.setVisible(false);			
			}			
		});
		
		
		SimpleButton WilfButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		WilfButton.setAutoFitSize(false);
		WilfButton.setText(SUB_APP_WILF);
		WilfButton.setBackgroundColour(Color.lightGray);
		WilfButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/wilf/wilf.xml", SUB_APP_WILF);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton LogicButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		LogicButton.setAutoFitSize(false);
		LogicButton.setText(SUB_APP_LOGIC);
		LogicButton.setBackgroundColour(Color.lightGray);
		LogicButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/logic/logic.xml", SUB_APP_LOGIC);
				menu.setVisible(false);
			}			
		});
		
		/*
		SimpleButton MaryAndArchieButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		MaryAndArchieButton.setAutoFitSize(false);
		MaryAndArchieButton.setText(SUB_APP_ARCHIE);
		MaryAndArchieButton.setBackgroundColour(Color.lightGray);
		MaryAndArchieButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/mary_and_archie/archie.xml", SUB_APP_ARCHIE);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton DanButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		DanButton.setAutoFitSize(false);
		DanButton.setText(SUB_APP_DAN_THE_DEALER);
		DanButton.setBackgroundColour(Color.lightGray);
		DanButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/dan/dan.xml", SUB_APP_DAN_THE_DEALER);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton CharlieButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		CharlieButton.setAutoFitSize(false);
		CharlieButton.setText(SUB_APP_FORGETFUL_CHARLIE);
		CharlieButton.setBackgroundColour(Color.lightGray);
		CharlieButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/charlie/charlie.xml", SUB_APP_FORGETFUL_CHARLIE);
				menu.setVisible(false);
			}			
		});
		
		SimpleButton MeetUpButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		MeetUpButton.setAutoFitSize(false);
		MeetUpButton.setText(SUB_APP_MEET_UP);
		MeetUpButton.setBackgroundColour(Color.lightGray);
		MeetUpButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected("data/mysteries/meet_up/meet_up.xml", SUB_APP_MEET_UP);
				menu.setVisible(false);
			}			
		});	
		
		SimpleButton SwimmingButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		SwimmingButton.setAutoFitSize(false);
		SwimmingButton.setText(SUB_APP_SWIM);
		SwimmingButton.setBackgroundColour(Color.lightGray);
		SwimmingButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/swimmer/swimmer.xml", SUB_APP_SWIM);
				menu.setVisible(false);
			}			
		});	
		/*
		SimpleButton schoolTripButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		schoolTripButton.setAutoFitSize(false);
		schoolTripButton.setText(SUB_APP_SCHOOL_TRIP);
		schoolTripButton.setBackgroundColour(Color.lightGray);
		schoolTripButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/school_trip_y5/school_trip_y5.xml", SUB_APP_SCHOOL_TRIP);
				menu.setVisible(false);				
			}			
		});	
		
		SimpleButton tripletsButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		tripletsButton.setAutoFitSize(false);
		tripletsButton.setText(SUB_APP_TRIPLETS);
		tripletsButton.setBackgroundColour(Color.lightGray);
		tripletsButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/triplets/triplets.xml", SUB_APP_TRIPLETS);
				menu.setVisible(false);		
			}			
		});	
				
		SimpleButton waltzerButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		waltzerButton.setAutoFitSize(false);
		waltzerButton.setText(SUB_APP_WALTZER);
		waltzerButton.setBackgroundColour(Color.lightGray);
		waltzerButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/waltzer_y5/waltzer_y5.xml", SUB_APP_WALTZER);
				menu.setVisible(false);			
			}			
		});	
		
		
		SimpleButton sneakyButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		sneakyButton.setAutoFitSize(false);
		sneakyButton.setText(SUB_APP_SNEAKYSYDNEY);
		sneakyButton.setBackgroundColour(Color.lightGray);
		sneakyButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {	
				for (SubAppMenuEventListener l:subAppMenuEventListener)
					l.menuSelected("data/mysteries/sneakysydney/sneakysydney.xml", SUB_APP_SNEAKYSYDNEY);
				menu.setVisible(false);			
			}			
		});
		
		menu.addSubItem(judithAndCatButton);
		menu.addSubItem(moneyTrailButton);
		menu.addSubItem(schoolTripButton);
		menu.addSubItem(tripletsButton);	
		menu.addSubItem(waltzerButton);
		menu.addSubItem(sneakyButton);
		
		menu.addSubItem(MaryAndArchieButton);
		menu.addSubItem(DanButton);
		menu.addSubItem(CharlieButton);
		menu.addSubItem(MeetUpButton);
		menu.addSubItem(SwimmingButton);
		*/
		
		menu.addSubItem(dinnerDisaster);
		menu.addSubItem(VLE);
		menu.addSubItem(sneakyButton);
		menu.addSubItem(schoolTripButton);
		menu.addSubItem(WilfButton);
		menu.addSubItem(LogicButton);

		return menu;
	}
	

	public void addSubAppMenuEventListener(SubAppMenuEventListener l){
		if (this.subAppMenuEventListener==null)
			this.subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
		
		if(!this.subAppMenuEventListener.contains(l))
			this.subAppMenuEventListener.add(l);
	}
	
	public void removeSubAppMenuEventListeners(){
		subAppMenuEventListener.clear();
	}
	
	public void removeSubAppMenuEventListener(SubAppMenuEventListener l){
		subAppMenuEventListener.remove(l);
	}
}
