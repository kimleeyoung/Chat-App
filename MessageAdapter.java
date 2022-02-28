package kr.ac.mjc.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> mMessageList;
    Context mContext;
    String userId;

    final int VIEWTYPE_MY=1;
    final int VIEWTYPE_OTHER=2;

    OnMessageClickListener mListener;

    public MessageAdapter(Context context,List<Message> messageList, String userId){
        this.mContext=context;
        this.mMessageList=messageList;
        this.userId=userId;
    }

    public void setOnMessageClickListener(OnMessageClickListener listener){
        this.mListener=listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEWTYPE_MY){
            View view=LayoutInflater.from(mContext).inflate(R.layout.item_message_my,parent,false);
            return new MyViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.item_message_other,parent,false);
            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message=mMessageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message=mMessageList.get(position);
        if(message.getWriterId().equals(userId)){
            return VIEWTYPE_MY;
        }
        else{
            return VIEWTYPE_OTHER;
        }
    }

    public class OtherViewHolder extends ViewHolder{

        TextView writerTv;
        TextView messageTv;
        TextView timeTv;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            writerTv=itemView.findViewById(R.id.writer_tv);
            messageTv=itemView.findViewById(R.id.message_tv);
            timeTv=itemView.findViewById(R.id.time_tv);
        }

        @Override
        void bind(Message message) {
            writerTv.setText(message.getWriterId());
            messageTv.setText(message.getText());
            timeTv.setText(message.getFormattedTime());
        }
    }

    public class MyViewHolder extends ViewHolder{

        TextView messageTv;
        TextView timeTv;
        Message mMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv=itemView.findViewById(R.id.message_tv);
            timeTv=itemView.findViewById(R.id.time_tv);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onMessageLongClick(mMessage);
                    return false;
                }
            });
        }

        @Override
        void bind(Message message) {
            mMessage=message;
            messageTv.setText(message.getText());
            timeTv.setText(message.getFormattedTime());
        }
    }

    abstract public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(Message message);
    }

    interface OnMessageClickListener{
        void onMessageLongClick(Message message);
    }
}
