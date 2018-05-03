package xysoft.im.swingDemo;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.*;

public class CompletableFutureDemo {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JLabel msglabel;
	ExecutorService executor = Executors.newFixedThreadPool(5);

	public CompletableFutureDemo() {
		prepareGUI();
	}

	public static void main(String[] args) {
		CompletableFutureDemo swingContainerDemo = new CompletableFutureDemo();
		swingContainerDemo.showJFrameDemo();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("CompletableFuture Examples");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);

		statusLabel.setSize(350, 100);

		msglabel = new JLabel("Welcome.", JLabel.CENTER);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	JFrame frame;
	private void showJFrameDemo() {

		headerLabel.setText("CompletableFuture action");

		frame = new JFrame();
		frame.setSize(300, 300);
		frame.setLayout(new FlowLayout());
		frame.add(msglabel);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				frame.dispose();
			}
		});
		JButton okButton = new JButton("Open a Frame");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setText("A Frame shown to the user.");
				frame.setVisible(true);
				//frame.setTitle(demoBlock()); //阻塞
				//frame.setTitle(demoNoBlock()); //不阻塞，但异常未处理
				//frame.setTitle(demoNoBlockException1()); //不阻塞，异常处理,但过程结果不回传
				frame.setTitle(demoNoBlockException2()); //不阻塞，异常处理合理，建议选择这种处理
			}
		});
		controlPanel.add(okButton);
		mainFrame.setVisible(true);

		
	}

	private String demoBlock() {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				try {
					TimeUnit.SECONDS.sleep(6);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "hello";
			}
		}, executor);

		try {
			System.out.println(resultCompletableFuture.get());
			return resultCompletableFuture.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	
	private String demoNoBlock() {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				try {
					TimeUnit.SECONDS.sleep(6);
					//int i = 1/0; 如果有异常，则永远也不返回，即永远得不到结果
					System.out.println("执行线程："+Thread.currentThread().getName());  
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "hello";
			}
		}, executor);

		resultCompletableFuture.thenAcceptAsync(new Consumer<String>() {  
		    @Override  
		    public void accept(String t) {  
		    System.out.println(t);  
		    System.out.println("回调线程："+Thread.currentThread().getName());  
		    frame.setTitle(t);
		    }  
		}, executor);  
		System.out.println("直接不阻塞返回了######");  
		return "直接不阻塞返回了######";
	}
	
	
	private String demoNoBlockException1() {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				try {
					TimeUnit.SECONDS.sleep(6);
					int i = 1/0;  //如果有异常，则永远也不返回，即永远得不到结果
					System.out.println("执行线程："+Thread.currentThread().getName());  
				} catch (ArithmeticException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "hello";
			}
		}, executor);

		resultCompletableFuture.thenAccept(new Consumer<String>() {  
		    @Override  
		    public void accept(String t) {  
		    System.out.println("accept--" + t);  
		    System.out.println(Thread.currentThread().getName());  
		    }  
		}).exceptionally(new Function<Throwable, Void>(){  
		    @Override  
		    public Void apply(Throwable t) {  
		    System.out.println(t.getMessage());  
		    frame.setTitle(t.getMessage());
		    return null;  
		    }  
		});  
		//resultCompletableFuture.completeExceptionally(new Exception("error"));  
		System.out.println("直接不阻塞返回了######");
		return "直接不阻塞返回了######";  
	}
	
	private String demoNoBlockException2() {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				try {
					TimeUnit.SECONDS.sleep(6);
					int i = 1/0;  //如果有异常，则永远也不返回，即永远得不到结果
					System.out.println("执行线程："+Thread.currentThread().getName());  
				} catch (ArithmeticException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "hello";
			}
		}, executor);

		resultCompletableFuture.exceptionally(new Function<Throwable, String>() {  
		    @Override  
		    public String apply(Throwable t) {  
		    System.out.println(t.getMessage());  
		    frame.setTitle(t.getMessage());
		    return t.getMessage();  
		    }  
		}).thenAccept(new Consumer<String>() {  
		    @Override  
		    public void accept(String t) {  
		    System.out.println("accept--" + t);  
		    System.out.println("accept--" + Thread.currentThread().getName());  
		    frame.setTitle(t);
		    }  
		});  
		//resultCompletableFuture.completeExceptionally(new Exception("error"));   
		System.out.println("直接不阻塞返回了######");
		return "直接不阻塞返回了######";  
	}
}
