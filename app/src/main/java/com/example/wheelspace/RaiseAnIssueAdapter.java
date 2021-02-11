package com.example.wheelspace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RaiseAnIssueAdapter extends RecyclerView.Adapter<RaiseAnIssueAdapter.FeedbackViewHolder>{
    ArrayList<Feedback> feedbackList;

    public RaiseAnIssueAdapter(ArrayList<Feedback> feedbackList){
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.feedback_list, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        holder.initialiseFeedbackVariables(feedbackList.get(position).getRouteFeedback(), feedbackList.get(position).getTimeFeedback() );
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder{

        View feedbackView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackView = itemView;
        }

        public void initialiseFeedbackVariables(String routeFeedback, String timeFeedback){
            TextView feedback_route = feedbackView.findViewById(R.id.feedback_route);
            TextView feedback_time = feedbackView.findViewById(R.id.feedback_time);

            feedback_route.setText(routeFeedback);
            feedback_time.setText(timeFeedback);
        }

    }

}
