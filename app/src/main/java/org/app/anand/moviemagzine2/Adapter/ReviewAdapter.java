package org.app.anand.moviemagzine2.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.anand.moviemagzine2.Model.Results;
import org.app.anand.moviemagzine2.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 10/20/2016.
 */

public class ReviewAdapter  extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listVenueHeader;
        private HashMap<String, List<Results>> _listChildVenues;

        public ReviewAdapter(Context context, List<String> listDataHeader,HashMap<String, List<Results>> listChildData) {
            this._context = context;
            this._listVenueHeader = listDataHeader;
            this._listChildVenues = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listChildVenues.get(this._listVenueHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null, true);
            TextView venueListChild = (TextView) convertView.findViewById(R.id.txt);
            ImageView posterView = (ImageView) convertView.findViewById(R.id.img);
            posterView.setVisibility(View.GONE);

            Results review = (Results) getChild(groupPosition, childPosition);
            venueListChild.append("Name: " + review.getAuthor() + "\n");
            venueListChild.append("Release Date: " + review.getContent() + "\n");


            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listChildVenues.get(this._listVenueHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listVenueHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listVenueHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
            String headerTitle = (String) this._listVenueHeader.get(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null, true);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }