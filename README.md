# WindparkPowerBC
Business control for windpark application

# Step 1
Copy or clone this repo into your project  

# Step 2 - Check build.gradle  
1. Open build.gradle  
2. Insert under dependencies this line:  
"implementation "commons-validator:commons-validator:1.6""  

# Step 3 - Implementation
1. Create the Object: private PowerValueControl bc;  

2. Initialize with your Max and Min Value: bc = new PowerValueControl(1000.00, 0.00);  

3. Set different Values for Control  
bc.setMandatory(true) //input mandatory  
bc.setReadOnly(true)  //input is not editable  

4. Do 4 Bindings: 
bc.powerValueOneProperty()    .bindBidirectional(model.powerOneProperty());   //your model, pm..  
bc.powerValueTwoProperty()    .bindBidirectional(model.powerTwoProperty());  
bc.powerValueThreeProperty()  .bindBidirectional(model.powerThreeProperty());  
bc.powerValueFourProperty()   .bindBidirectional(model.powerFourProperty());  

# Contact
marc.schnydrig@students.fhnw.ch  
koray.oezkaynak@students.fhnw.ch
