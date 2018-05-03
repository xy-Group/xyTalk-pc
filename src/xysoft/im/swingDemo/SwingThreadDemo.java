package xysoft.im.swingDemo;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class SwingThreadDemo {

	public SwingThreadDemo() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {


		//费时操作，线程模型
		Runnable timeConsumingRunnable = new Runnable()
    	{
    	   public void run() 
    	   {
    	      // Execute time-consuming task
    		   try {
        			Thread.sleep(9000);
        		} catch (InterruptedException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
    	      // Execute UI updates on event-dispatch thread
    	      SwingUtilities.invokeLater(
    	         new Runnable() 
    	         {
    	            public void run()
    	            {
    	               // Update UI
    	               //usernameField.setText("aaaaa");
    	            }
    	         });
    	    }
    	};
    	new Thread(timeConsumingRunnable).start();
    	
		//费时操作，SwingWorker模型
    	SwingWorker aWorker = new SwingWorker() 
    	{
		@Override
		protected Object doInBackground() throws Exception {
			// Execute time-consuming task
			try {
    			Thread.sleep(9000);
    		} catch (InterruptedException e1) {
    			e1.printStackTrace();
    		}
			return false;
		}
		@Override
        protected void done() {
            boolean bStatus = false;
            try {
                bStatus = (boolean) get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Update UI
            //usernameField.setText("aaaaa");
            System.out.println("Finished with status " + bStatus);
        }
    	};
    	aWorker.execute();
    	
    	//费时操作，SwingWorker模型,带泛型类型
		ab();

	}

	private static void ab() {
		//费时操作，SwingWorker模型,带泛型类型
       	SwingWorker<String, String> worker = new SwingWorker<String, String>() 
    	{
		@Override
		protected String doInBackground() throws Exception {
			try {
    			Thread.sleep(9000);
    		} catch (InterruptedException e1) {
    			e1.printStackTrace();
    		}
			return "ok";
		}
		@Override
        protected void done() {
            String bStatus = "";
            try {
                bStatus =  (String) get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Update UI
            //usernameField.setText(bStatus);
            System.out.println("Finished with status " + bStatus);
        }
    	};
    	worker.execute();
	}

}
