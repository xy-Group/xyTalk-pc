package xysoft.im.adapter;

import javax.swing.*;


public class ContactsHeaderViewHolder extends HeaderViewHolder
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4374823571943802821L;
	private String letter;
    public JLabel letterLabel;

    public ContactsHeaderViewHolder(String ch)
    {
        this.letter = ch;
    }


    public String getLetter()
    {
        return letter;
    }
}
