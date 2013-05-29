package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import domain.ChatMessage;

/**
 * This class defines the cellrendering of the messagelist.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/10/2012
 */
public class MessageCellRendering implements ListCellRenderer<Object> {

    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        
        ChatMessage cm = (ChatMessage)value;
        
        JPanel template = new JPanel(new BorderLayout());
        template.setOpaque(false);
        
        if(index > 0) {
            template.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        }
        
        Font boldFont = new Font(template.getFont().getName(), Font.BOLD, template.getFont().getSize());
        Font dateFont = new Font(template.getFont().getName(), Font.PLAIN, template.getFont().getSize()-2);
        
        JLabel lblUser = new JLabel(String.format("%s: ", cm.getUser().getName()));
        lblUser.setFont(boldFont);
        
        JLabel lblMessage = new JLabel(cm.getMessage());
        
        JLabel lblDate = new JLabel(formatter.format(cm.getDate()));
        lblDate.setForeground(Color.gray);
        lblDate.setFont(dateFont);
        
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setOpaque(false);
        pnlTop.add(lblUser);
        pnlTop.add(lblMessage);
        
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBottom.setOpaque(false);
        pnlBottom.add(lblDate);
        
        template.add(pnlTop, BorderLayout.CENTER);
        template.add(pnlBottom, BorderLayout.SOUTH);
        
        return template;
    }

}
