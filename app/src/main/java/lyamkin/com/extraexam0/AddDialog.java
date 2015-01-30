package lyamkin.com.extraexam0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle(getActivity().getResources().getString(R.string.add_title));

        LayoutInflater inflater = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(view);

        final EditText editTitle = (EditText) view.findViewById(R.id.editTitle);

            dialogBuilder.setPositiveButton(getActivity().getResources().getString(R.string.add_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContentValues values = new ContentValues();
                    values.put("name", editTitle.getText().toString());
                    values.put("status", "Waiting");
                    values.put("count", 0);
                    values.put("time", 0);
                    getActivity().getContentResolver().insert(TaskContentProvider.CHANNELS_URI, values);
                    dismiss();
                }
            });

        dialogBuilder.setNegativeButton(getActivity().getResources().getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return dialogBuilder.create();
    }
}
