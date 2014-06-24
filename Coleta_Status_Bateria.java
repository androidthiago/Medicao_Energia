package example.statusbattery;

import java.io.BufferedReader;

public class MainActivity extends Activity {
	
	public TextView txt_status;
	public TextView txt_temperature;
	public TextView txt_technology;
	public TextView txt_voltage;
	
	/*Chronometer m_chronometer;
	Button btn_Start, btn_Pause, btn_Reset;
	boolean isClickPause = false;
	long tempoQuandoParado = 0;*/
	
	public EditText edt_time;
	public Spinner escale;
	public Button btn_Start, btn_Pause, btn_Reset;
	public TextView txt_crono;
	
	private long startTime = 0L; 
	private Handler customHandler = new Handler();
		 
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	private static final String[] ESCALAS = new String[]{"MILI-SEG", "SEGUNDOS", "MINUTOS", "HORAS"};
	
    boolean pause=false;
        
    //public TextView txt_teste = (TextView) findViewById(R.id.textView7);
       
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Spinner combo = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ESCALAS);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        combo.setAdapter(adp);      
		
		String a1;
		String a2;
		String a3;
		
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter); //this.
        
        this.registerReceiver(this.myBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPct 		= level; /// (float)scale;
        String batteryTech 	= batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        float batteryTemp 	= batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)/10;
        float batteryVolt	= batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)/1000;
        
        txt_status 		= (TextView) findViewById(R.id.textView1);
        txt_technology 	= (TextView) findViewById(R.id.textView3);
        txt_temperature = (TextView) findViewById(R.id.textView4);
        txt_voltage 	= (TextView) findViewById(R.id.textView5);
        //btnSalvar = (Button) findViewById(R.id.button1);
        
        a1=String.valueOf(batteryPct);
        a2="%";
        a3=a1.concat(a2);
        
        txt_status.setText(a3);
        txt_technology.setText(batteryTech);
        txt_temperature.setText(batteryTemp + "°C");
        txt_voltage.setText(batteryVolt + "v");
        
        SalvarArquivo();
        
        btn_Start = (Button) findViewById(R.id.button2);
        btn_Pause = (Button) findViewById(R.id.button4);
        btn_Reset = (Button) findViewById(R.id.button3);
        
        txt_crono = (TextView) findViewById(R.id.textView6);
        
        edt_time = (EditText) findViewById(R.id.editText1);
        
        int tempo=Integer.parseInt(edt_time.getText().toString());
        int escala=combo.getSelectedItemPosition();
        
        final Temporizador temp = new Temporizador();
        
        btn_Start.setOnClickListener(new View.OnClickListener() {
        		  	public void onClick(View view) {
        		  		startTime = SystemClock.uptimeMillis();
        		  		customHandler.postDelayed(updateTimerThread, 0);
        		              
        		         pause=false;
        		         //temp.temporize(5, 1);
        		  	}
        });
        
        btn_Pause.setOnClickListener(new View.OnClickListener() {
        		  	public void onClick(View view) {
        		  		timeSwapBuff += timeInMilliseconds;
        		  		customHandler.removeCallbacks(updateTimerThread);
        		              
        		  		pause=true;
        		  		//temp.temporize(5, 1);
        		  	}
        });
        
        btn_Reset.setOnClickListener(new View.OnClickListener() {
  		  			public void onClick(View view) {
  		  				customHandler.removeCallbacks(updateTimerThread);
  		  				
  		  				timeInMilliseconds = 0L;
  		  				timeSwapBuff = 0L;
  		  				updatedTime = 0L;
  		  				startTime = 0L; 
  		  				txt_crono.setText("00:00:00");
  		      	
  		  				pause=true;
  		  				//temp.temporize(5, 1);
  		  				
  		  				edt_time.setText("00");
  		  				
  		  }
        });
        
       /* int tempo = (1000 * 60) * 5;   // 5 min.  
        int periodo = 100;  // quantidade de vezes a ser executado.  
        Timer timer = new Timer();  
          
        timer.scheduleAtFixedRate(  
                new TimerTask() {  
                    public void run() {  
                        //aqui vai o código que deve ser executado  
                    }  
                }, tempo, periodo);  */
        
        /*
        //Utilizando o Cronometro
        m_chronometer = (Chronometer) findViewById(R.id.chronometer1);
        btn_Start = (Button) findViewById(R.id.button2);
        btn_Pause = (Button) findViewById(R.id.button4);
        btn_Reset = (Button) findViewById(R.id.button3);
        
        //Métodos do crônometro
        
        edt_time = (EditText) findViewById(R.id.editText1);
        escale = (Spinner) findViewById(R.id.spinner1);
        
        
        //Botão INICIAR
        btn_Start.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		if(isClickPause){ 
        			m_chronometer.setBase(SystemClock.elapsedRealtime() + tempoQuandoParado);
        			m_chronometer.start();
        			tempoQuandoParado = 0;
        			isClickPause = false;
        		}
        		else{
        			m_chronometer.setBase(SystemClock.elapsedRealtime());
        			m_chronometer.start();
        			tempoQuandoParado = 0;
        		}

        	}
        });
      
        //Botão PAUSAR
        btn_Pause.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		if(isClickPause == false){ //entra para false;
        			tempoQuandoParado = m_chronometer.getBase() - SystemClock.elapsedRealtime();
        		}
        		m_chronometer.stop();
        		isClickPause = true;   
        	}
        });
        
        //Botão RESETAR
        	btn_Reset.setOnClickListener(new View.OnClickListener() {

        		@Override
        		public void onClick(View arg0) {
        			m_chronometer.stop();
        			m_chronometer.setText("00:00");
        			tempoQuandoParado = 0;
        		}
        	});
       
      */
       
	}
	
	//cronometro
	private Runnable updateTimerThread = new Runnable() {
			public void run() {
		 
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			 
				updatedTime = timeSwapBuff + timeInMilliseconds;
			 
				int secs = (int) (updatedTime / 1000);
				int mins = secs / 60;
				secs = secs % 60;
				int milliseconds = (int) (updatedTime % 1000);
				txt_crono.setText("" + mins + ":"
			              		+ String.format("%02d", secs) + ":"
			                    + String.format("%03d", milliseconds));
			            customHandler.postDelayed(this, 0);
		    }
			 
	};
	
	//controlando o temporizador
	public class Temporizador {
	    Timer timer;

	    public Temporizador() {
	        
	    }
	    
	    public void temporize(int xtempo, int xescala){
	    	timer = new Timer();
	    	
	    	if (xescala==1){ //Segundos
	    		timer.schedule(new RemindTask(),
	    						0,        //initial delay
	    						xtempo*1000);  //subsequent rate
	    	}
	    }

	    class RemindTask extends TimerTask {
	        public void run() {
	            if (!pause) {
	                SalvarArquivo();
	                Toast.makeText(getBaseContext(), "Bateria: " + txt_status.getText(), Toast.LENGTH_SHORT).show();
	            } else {
	                timer.cancel(); // Not necessary because
	                                  // we call System.exit
	                //System.exit(0);   // Stops the AWT thread 
	                                  // (and everything else)
	            }
	        }
	    }
	}
	
	//notification status battery
	private BroadcastReceiver myBatteryReceiver = new BroadcastReceiver(){
	        @Override
	        public void onReceive(Context context, Intent intent) {
	           int nBateria = intent.getIntExtra("level", 0); // Nivel de bateria
	           Toast.makeText(context, "Bateria: " + nBateria, Toast.LENGTH_SHORT).show();
	            
	        }
	 };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    
    //manipulando o TXT
    private void SalvarArquivo()
    {
        String lstrNomeArq;
        File arq;
        String cabecalho;
        byte[] dados;
        String text_exist=""; 
        String lstrlinha;
        int aux=0;
        Button btn_teste;
        
        btn_teste = (Button) findViewById(R.id.button1);
        
        try
        {
        	lstrNomeArq="statusBattery.txt";
        	cabecalho="Informações da Bateria:\n\n";
        	//pega o nome do arquivo a ser gravado
            
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            
            if (arq.exists()){
            	//btn_teste.setVisibility(1);
    	    	aux=1;
    	    	cabecalho="";
            	
    	    	BufferedReader br = new BufferedReader(new FileReader(arq));
            	
            	while ((lstrlinha = br.readLine()) != null) {
            		text_exist=text_exist + lstrlinha + "\n";
                }  
            	
            	br.close();
            }
            
            FileOutputStream fos;
            fos = new FileOutputStream(arq);
            
            //transforma o texto digitado em array de bytes
            if (aux==0){
            dados = new String(cabecalho 
            		 	+ "Status: "     	+ txt_status.getText().toString() 		+ "\n"
            			+ "Technology: " 	+ txt_technology.getText().toString() 	+ "\n" 
             			+ "Temperature: "	+ txt_temperature.getText().toString() 	+ "\n" 
            			+ "Voltage: " 		+ txt_voltage.getText().toString() 		+ "\n").getBytes();
            }else{
            	 dados = new String(text_exist + "\n"
            		 	     + "Status: "     	+ txt_status.getText().toString() 		+ "\n"
            		 	     + "Technology: " 	+ txt_technology.getText().toString() 	+ "\n" 
            		 	     + "Temperature: "	+ txt_temperature.getText().toString() 	+ "\n" 
            		 	     + "Voltage: " 		+ txt_voltage.getText().toString() 		+ "\n").getBytes();
            }        
  
            //escreve os dados e fecha o arquivo
            fos.write(dados);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }       
    }
    
    private void trace (String msg)
    {
        toast (msg);
    } 
    
    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    } 
    
    private String GetRoot()
    {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.toString();
    }
   

}

