package step.email;

import java.util.Date;

import android.widget.TableRow.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.widget.TextView;
import javax.mail.Message;
import javax.mail.Address;

public class TableModel {
	private TableLayout tableLayout;
	TableModel(TableLayout t1){
		this.tableLayout = t1;
	}
	public void setMessages(Message[] msgs) throws Exception{
		int i, j, max_rows;
		Address[] a;
		Date d;
		TextView t;
		TableRow row;
		i = tableLayout.getChildCount();
		if(i<5){
			max_rows = i;
		}
		else{
			max_rows = 6;
		}

		for(i=0; i<max_rows; i++){
			row = (TableRow)tableLayout.getChildAt(2+i);
			t = (TextView)row.getChildAt(0);
			a = msgs[i].getFrom();
			t.setText(a[0].toString());
			
			t = (TextView)row.getChildAt(1);
			t.setText(msgs[i].getSubject());
			
			t = (TextView)row.getChildAt(2);
			d = msgs[i].getSentDate();
			t.setText(d.toString());
			
		}
	}
}
