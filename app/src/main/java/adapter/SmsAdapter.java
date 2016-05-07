package adapter;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.messager.R;

import java.util.ArrayList;


/**
 * Created by Shubham Gupta on 30-04-2016.
 *
 * An adapter to get the messages list and show them using recycler view
 */
public class SmsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String letter;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    ArrayList<String[]> message;


    public SmsAdapter(ArrayList<String[]> model) {
        message = new ArrayList<>();
        message = model;

    }

    /**
     * Courtesy: Vladimir Topalovic
     *
     * @param holder
     * @param goesDown
     */
    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown) {
        int holderHeight = holder.itemView.getHeight();
        holder.itemView.setPivotY(goesDown ? 0 : holderHeight);
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown ? 300 : -300, 0);
        animatorTranslateY.setDuration(500);
        animatorTranslateY.start();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_view, parent, false);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.title.setText(message.get(position)[0]);
        itemHolder.text.setText(message.get(position)[1]);
        letter = String.valueOf(itemHolder.title.getText().charAt(0));
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        itemHolder.letter.setImageDrawable(drawable);
        animate(holder, true);


    }

    @Override
    public int getItemCount() {
        return message.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView letter;
        TextView title, text;

        public ItemHolder(View itemView) {
            super(itemView);
            letter = (ImageView) itemView.findViewById(R.id.letter);
            title = (TextView) itemView.findViewById(R.id.text_title);
            text = (TextView) itemView.findViewById(R.id.show_text);
        }


    }

}
