package xysoft.im.service;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat2.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import xysoft.im.app.Launcher;

public class FormService {

	public FormService() {

		
	}

	@SuppressWarnings("deprecation")
	public static void createForm() {
		// Create a new form to gather data
		Form formToSend = new Form(DataForm.Type.form);
		formToSend.setInstructions("Fill out this form to report your case.\nThe case will be created automatically.");
		formToSend.setTitle("Case configurations");
		
		// Add a hidden variable to the form
		FormField field = new FormField("hidden_var");
		field.setType(FormField.Type.hidden);
		field.addValue("Some value for the hidden variable");
		formToSend.addField(field);
		
		// Add a fixed variable to the form
		field = new FormField();
		field.addValue("Section 1: Case description");
		formToSend.addField(field);
		
		// Add a text-single variable to the form
		field = new FormField("name");
		field.setLabel("Enter a name for the case");
		field.setType(FormField.Type.text_single);
		formToSend.addField(field);
		
		// Add a text-multi variable to the form
		field = new FormField("description");
		field.setLabel("Enter a description");
		field.setType(FormField.Type.text_multi);
		formToSend.addField(field);
		
		
		// Create a chat
		ChatManager chatManager = org.jivesoftware.smack.chat2.ChatManager.getInstanceFor(Launcher.connection);
		EntityBareJid jid;
		try {
			jid = JidCreate.entityBareFrom("test1@win7-1803071731");
			Chat chat = chatManager.chatWith(jid);
			Message msg = new Message();
			msg.setBody("To enter a case please fill out this form and send it back");
			// Add the form to fill out to the message to send
			msg.addExtension(formToSend.getDataFormToSend());
			// Send the message with the form to fill out
			try {
				chat.send(msg);
			} catch (NotConnectedException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (XmppStringprepException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

}
