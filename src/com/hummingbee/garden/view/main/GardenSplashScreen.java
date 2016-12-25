package com.hummingbee.garden.view.main;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.controller.OperationController;
import com.hummingbee.garden.controller.SprinklerStatusTableModeListener;
import com.hummingbee.garden.controller.WaterUsageController;
import com.hummingbee.garden.controller.WeeklyPlanButtonListener;
import com.hummingbee.garden.controller.WeeklyPlanGroupChooseListener;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;
import com.hummingbee.garden.model.simulation.TemperatureSimulator;
import com.hummingbee.garden.model.sprinkler.Sprinkler;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;
import com.hummingbee.garden.model.sprinkler.TemperaturePlan;
import com.hummingbee.garden.model.sprinkler.WeeklyPlan;
import com.hummingbee.garden.view.overview.OverViewPanel;
import com.hummingbee.garden.view.plan.PlanPanel;
 
public class GardenSplashScreen extends Frame{
	public static final String TITLE = "HummingBee Home Garden System";
	
	private SprinklerBank sprinklerBank = null;
	private WeeklyPlan weeklyPlan = null;
	private TemperaturePlan temperaturePlan = null;
	
	private DateTimeSimulator dateTimeSimulator = null;
	private TemperatureSimulator temperatureSimulator = null;
	
	private OverViewPanel overViewPanel = null;
	private PlanPanel planPanel = null;
	
    void renderSplashFrame(Graphics2D graphics, String msgString, int yCount) {
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(10,240,250,60);
        graphics.setPaintMode();
        graphics.setColor(Color.BLACK);
        graphics.setFont(Utils.getFormatFont(Font.BOLD | Font.ITALIC, 20));
        
        graphics.drawString(msgString, 10, 130 + yCount*18);
    }
    
    public GardenSplashScreen() {
        super(TITLE);
        
        this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				storeData();
				System.exit(0);
            }
		});

		
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            return;
        }
        Graphics2D graphics = splash.createGraphics();
        if (graphics == null) {
            return;
        }
        
        try{
        	renderSplashFrame(graphics, "Loading Sprinklers from Database...", 0);
        	splash.update();
        	sprinklerBank = new SprinklerBank();
        	
        	dateTimeSimulator = new DateTimeSimulator();
        	temperatureSimulator = new TemperatureSimulator(sprinklerBank.getSprinklerGroups());
        	
        	sprinklerBank.setSprinklerDatetimeSimulator(dateTimeSimulator);
        	sprinklerBank.setSprinklerTemperatureSensor(temperatureSimulator);
        	
        	Thread.sleep(1000);
        	
        	renderSplashFrame(graphics, "Loading Weekly Plan from Database...", 1);
        	splash.update();
        	weeklyPlan = new WeeklyPlan(sprinklerBank.getSprinklerGroups());
        	Thread.sleep(1000);
        	
        	renderSplashFrame(graphics, "Loading Tempature Plan from Database...", 2);
        	splash.update();
        	temperaturePlan = new TemperaturePlan();
        	Thread.sleep(1000);
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        
        splash.close();
        
        // Initialize view components
        initPanel();
        
		setSize(Utils.FRAME_WIDTH, Utils.FRAME_HEIGHT);
		pack();
        setVisible(true);
        toFront();
        
        // Initialize sprinkler models, observers and action listeners
        initSprinklers();
    }
    
    private void initPanel(){
    	JTabbedPane mainPane = new JTabbedPane();
    	mainPane.setFont(Utils.getFormatFont(Font.BOLD, 16));
    	add(mainPane);
    	
    	overViewPanel = new OverViewPanel(sprinklerBank);
    	mainPane.addTab(OverViewPanel.TITLE, overViewPanel);
    	
    	planPanel = new PlanPanel(weeklyPlan, temperaturePlan, sprinklerBank.getSprinklerGroups());
    	mainPane.addTab(planPanel.TITLE, planPanel);
    }
    
    private void initSprinklers(){
    	sprinklerBank.setSprinklerPlan(weeklyPlan, temperaturePlan);
    	sprinklerBank.setSprinklerDatetimeSimulator(this.dateTimeSimulator);
    	sprinklerBank.setSprinklerTemperatureSensor(temperatureSimulator);
    	
    	this.addObservers();
        this.addActionListeners();
        
        sprinklerBank.startSprinklerThreads();
    }
    
    private void addObservers(){
    	sprinklerBank.addObserver(overViewPanel.getSprinklerStatusPanel());
    	sprinklerBank.addObserver(overViewPanel.getSprinklerMapPanel());
    	sprinklerBank.addObserverToSprinklers(overViewPanel.getSprinklerMapPanel());
    	sprinklerBank.addObserverToSprinklers(overViewPanel.getSprinklerStatusPanel());
    }
    
    private void addActionListeners(){
    	// Add table mode listener for the sprinkler status table
    	SprinklerStatusTableModeListener spkTableModeListener = new SprinklerStatusTableModeListener(sprinklerBank);
    	overViewPanel.getSprinklerStatusPanel().addTableModeListener(spkTableModeListener);
    	
    	// Add operation controller to listen for turning on/off sprinklers and simulation
    	JComboBox groupComboBox = overViewPanel.getOperationPanel().getGroupComobBox();
    	OperationController operationController = new OperationController(sprinklerBank, groupComboBox, temperatureSimulator, dateTimeSimulator);
    	overViewPanel.getOperationPanel().addOperationActionListener(operationController);
    	
    	// Add water usage panel controller
    	WaterUsageController waterUsageController = new WaterUsageController(overViewPanel.getWaterUsagePanel().getMonthlyBarPanel());
    	overViewPanel.getWaterUsagePanel().getMonthlyBarPanel().addSprinklerChooseListener(waterUsageController);
    	
    	// Add weekly plan controller
    	WeeklyPlanButtonListener weeklyPlanButtonListener = new WeeklyPlanButtonListener(planPanel);
    	planPanel.addPlanButtonListener(weeklyPlanButtonListener);
    	
    	WeeklyPlanGroupChooseListener weeklyPlanGroupChooseListener = new WeeklyPlanGroupChooseListener(planPanel);
    	planPanel.addGroupChooseListener(weeklyPlanGroupChooseListener);
    }
    
    private void storeData(){
    	sprinklerBank.storeSprinklerStatus();
    	weeklyPlan.storeWeeklyPlan();
    	for(Sprinkler sprinkler: sprinklerBank.getAllSprinklers()){
    		sprinkler.getWaterUsage().storeWaterUsage();
    	}
    }
}
