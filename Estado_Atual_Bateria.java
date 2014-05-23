protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	
	//obtendo o estado atual da bateria
	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

	//Variáveis para inserir os dados relativos a situação atual da bateria
        int batteryPct 		= batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        String batteryTech 	= batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        float batteryTemp 	= batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)/10;
        float batteryVolt	= batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)/1000;

	//instanciando objetos para manipular os TextViews da Activity Main 
 	txt_status 	= (TextView) findViewById(R.id.textView1);
        txt_technology 	= (TextView) findViewById(R.id.textView3);
        txt_temperature = (TextView) findViewById(R.id.textView4);
        txt_voltage 	= (TextView) findViewById(R.id.textView5);

 	String a1=String.valueOf(batteryPct);
        String a2="%";
        String a3=a1.concat(a2);
        
	//Setando valores para serem vizualizados no Menu do Aplicativo 
        txt_status.setText(a3);
        txt_technology.setText(batteryTech);
        txt_temperature.setText(batteryTemp + "°C");
        txt_voltage.setText(batteryVolt + "v");
        
	//Gerando o arquivo .TXT com as informações da bateria.
        SalvarArquivo();

}

private void SalvarArquivo()
    {
        String lstrNomeArq;
        File arq;
        String cabecalho;
        byte[] dados;
        try
        {
            //pega o nome do arquivo a ser gravado
            lstrNomeArq = "statusBaterry.txt";
             
            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            FileOutputStream fos;
             
            cabecalho="Informações da Bateria:\n\n";
            
            //transforma o texto digitado em array de bytes
            dados = new String(cabecalho 
            		 	+ "Status: "     	+ txt_status.getText().toString() 	+ "\n"
            			+ "Technology: " 	+ txt_technology.getText().toString() 	+ "\n" 
             			+ "Temperature: "	+ txt_temperature.getText().toString() 	+ "\n" 
            			+ "Voltage: " 		+ txt_voltage.getText().toString() 	+ "\n").getBytes();
            
            fos = new FileOutputStream(arq);
             
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
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    } 

