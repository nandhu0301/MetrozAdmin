package com.smiligenceUAT1.metrozadmin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.common.Constant;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anupamchugh on 22/12/17.
 */


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        if (childText.equals(Constant.APPROVE_STORE_PARTNER)) {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_approveseller, 0, 0, 0 );
        }
        else if (childText.equals(Constant.APPROVE_DELIVERY_PARTNER)) {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_approvedeliveryboy, 0, 0, 0 );
        }else if (childText.equals(Constant.APPROVE_ITEMS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_itemapprove_new, 0, 0, 0 );
        } else if (childText.equals(Constant.MAINTAIN_OFFERS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_add_discounts, 0, 0, 0 );
        }else if (childText.equals(Constant.VIEW_ORDERS_ADD_COUPON))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_vieworders, 0, 0, 0 );
        }else if (childText.equals(Constant.ADD_ADVERTISEMNETS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_add_adv, 0, 0, 0 );
        }else if (childText.equals(Constant.TITLE_VIEW_ADVERTISEMENTS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_viewadvertisements, 0, 0, 0 );
        }else if (childText.equals(Constant.TITLE_CATEGORY))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_categories_svg, 0, 0, 0 );
        }else if (childText.equals(Constant.TITLE_SUBCATEGORY))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_subcategory_svg, 0, 0, 0 );
        }else if (childText.equals(Constant.MAINTAIN_TIPS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_addtips, 0, 0, 0 );
        }else if (childText.equals(Constant.MAINTAIN_DELIVERY_FARE))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_del, 0, 0, 0 );
        }else if (childText.equals(Constant.DELIVERY_DETAILS))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_contact, 0, 0, 0 );
        }else if (childText.equals(Constant.BULK_UPLOAD))
        {
            txtListChild.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_bulkupload, 0, 0, 0 );
        }

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)

            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }



        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        TextView lblListHeader1 = convertView.findViewById(R.id.lblListHeader1);
        if (headerTitle.equals(Constant.TITLE_DASHBOARD ) ||headerTitle.equals(Constant.ASSIGN_ORDERS_FOR_DELIVERY )
        || headerTitle.equals(Constant.DELIVERY_BOY_HISTORY ) || headerTitle.equals(Constant.STORE_HISTORY ) ||
                headerTitle.equals(Constant.TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN ) || headerTitle.equals(Constant.TITLE_USER ) ||
                headerTitle.equals(Constant.TITLE_CONTACT_US ) ||headerTitle.equals("Logout" ))
        {
            if (headerTitle.equals(Constant.TITLE_DASHBOARD))
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_dashboardicon_01, 0, 0, 0 );

            }else if (headerTitle.equals(Constant.ASSIGN_ORDERS_FOR_DELIVERY ))
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_assignorders, 0, 0, 0 );
            }
            else if ( headerTitle.equals(Constant.DELIVERY_BOY_HISTORY ) )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_deliveryboyhistory, 0, 0, 0 );
            }
            else if ( headerTitle.equals(Constant.STORE_HISTORY ) )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_storehistory_01, 0, 0, 0 );
            }
            else if ( headerTitle.equals(Constant.TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN ) )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_cashwallet_01, 0, 0, 0 );
            }
            else if ( headerTitle.equals(Constant.TITLE_USER ) )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_useradmin_svg, 0, 0, 0 );
            }
            else if ( headerTitle.equals(Constant.TITLE_CONTACT_US ) )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_contactus, 0, 0, 0 );
            }
            else if ( headerTitle.equals("Logout") )
            {
                lblListHeader1.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_logout_svg, 0, 0, 0 );
            }
            lblListHeader1.setText(headerTitle);
            lblListHeader.setVisibility(View.INVISIBLE);
            lblListHeader1.setVisibility(View.VISIBLE);
        }else
        {
            if (headerTitle.equals(Constant.Approvals))
            {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_approved_icon, 0, R.drawable.ic_outline_keyboard_arrow_down_24, 0 );
            }
            else if (headerTitle.equals(Constant.ordersAndOffers))
            {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_ordersandoffers_icon, 0, R.drawable.ic_outline_keyboard_arrow_down_24, 0 );
            }
            else if (headerTitle.equals(Constant.advertisments))
            {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_advertisements_icon, 0, R.drawable.ic_outline_keyboard_arrow_down_24, 0 );
            }
            else if (headerTitle.equals(Constant.MaintainingInputs))
            {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_maintain_inputs_icon, 0, R.drawable.ic_outline_keyboard_arrow_down_24, 0 );
            }
            lblListHeader.setText(headerTitle);
            lblListHeader.setVisibility(View.VISIBLE);
            lblListHeader1.setVisibility(View.INVISIBLE);
        }


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