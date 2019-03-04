package giftadeed.kshantechsoft.com.giftadeed.Help;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows FAQ                                                //
/////////////////////////////////////////////////////////////////
public class Help extends Fragment {

    static android.support.v4.app.FragmentManager fragmgr;
    View rootview;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Button btnhelp;
    Typeface fontOfstandardtext;
    TextView txttitle;
    TextView txtneedmore;
    ImageView imgtoolbar_help;

    public static Help newInstance(int sectionNumber) {
        Help fragment = new Help();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_help, container, false);
        //---------------------------------------------------------------------------
        TaggedneedsActivity.fragname = Help.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.help_heading));
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        fragmgr = getFragmentManager();
        expListView = (ExpandableListView) rootview.findViewById(R.id.lvExp);
        expListView.setFooterDividersEnabled(true);
        expListView.addFooterView(new View(expListView.getContext()));
        prepareListData();
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });
// Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/

                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;


            }
        });


        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
               /* Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                return false;
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });

        return rootview;
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What social issue does GAD plan to address?");
        listDataHeader.add("Is this app accessible globally?");
        listDataHeader.add("What information I share is public to other users? Can I maintain privacy as a user?");
        listDataHeader.add("How do I tag the needy person?");
        listDataHeader.add("How do I know that the needs of the person I tagged have been fulfilled?");
        listDataHeader.add("Is there any monetary exchange expected while fulfilling a need?");
        listDataHeader.add("Can I fulfill a need that has already been fulfilled?");
        listDataHeader.add("Who all get notified once I tag a need?");
        listDataHeader.add("Why does the need I tag do not show up after some time?");
        listDataHeader.add("How are the Reward points calculated?");
        listDataHeader.add("What option do I have to report a need I think is not genuine?");
        listDataHeader.add("I am not receiving any emails from Gift-A-Deed. What do I do?");


        // Adding child data
        List<String> first = new ArrayList<String>();
        // first.add("Gift-A-Deed app hopes to bridge the gap between the ‘haves’ and the‘have-nots’ with regards to life’s basic necessities.");
        first.add(getString(R.string.first));

        List<String> second = new ArrayList<String>();
        //second.add("Currently, this is app is available only in Hong Kong, India, Canada, China, France, and Thailand. We do plan to launch this app world-wide in the coming future.");
        second.add(getString(R.string.second));

        List<String> third = new ArrayList<String>();
        // third.add("Only your Full Name will be visible to others. If you wish, you can go Anonymous by changing the Privacy Settings by visiting the My Profile option.");
        third.add(getString(R.string.third));

        List<String> fourth = new ArrayList<String>();
        // fourth.add("To tag a Needy person, just go to the Tag a Deed section of the app. On the Tag a Deed page, fill in the required information, and click a photo of the needy person (optional), and you are all set to post the tag.");
        fourth.add(getString(R.string.fourth));

        List<String> fifth = new ArrayList<String>();
        // fifth.add("Once your tag has been fulfilled, you will get a notification regarding the same.");
        fifth.add(getString(R.string.fifth));

        List<String> sixth = new ArrayList<String>();
        // sixth.add("No. There is no monetary exchange expected while fulfilling a need.");
        sixth.add(getString(R.string.sixth));

        List<String> seventh = new ArrayList<String>();
        //seventh.add("No. You cannot fulfill a need that has already been fulfilled.");
        seventh.add(getString(R.string.seventh));

        List<String> eighth = new ArrayList<String>();
        // eighth.add("All the Gift-A-Deed app users who are in a vicinity of 10 km from the tag will get notified once you tag a deed.");
        eighth.add(getString(R.string.eighth));


        List<String> nineth = new ArrayList<String>();
        //nineth.add("All needs have a predefined validity that is set during tagging the need itself. All the needs that have passed the validity stop being shown in the app. Also, whenever a tag is fulfilled, it stops being shown in the app.");
        nineth.add(getString(R.string.nineth));

        List<String> tenth = new ArrayList<String>();
        // tenth.add("– For every successful tag, the user earns 100 reward points. Similarly, for every fulfillment by the user, the user earns 200 reward points.");
        tenth.add(getString(R.string.tenth));

        List<String> eleventh = new ArrayList<String>();
        //eleventh.add("You can report a user, or a need to the admin by going to the details page of that need.");
        eleventh.add(getString(R.string.eleventh));

        List<String> twelvth = new ArrayList<String>();
        //eleventh.add("You can report a user, or a need to the admin by going to the details page of that need.");
        twelvth.add(getString(R.string.twelvth));

        listDataChild.put(listDataHeader.get(0), first); // Header, Child data
        listDataChild.put(listDataHeader.get(1), second);
        listDataChild.put(listDataHeader.get(2), third);
        listDataChild.put(listDataHeader.get(3), fourth);
        listDataChild.put(listDataHeader.get(4), fifth);
        listDataChild.put(listDataHeader.get(5), sixth);
        listDataChild.put(listDataHeader.get(6), seventh);
        listDataChild.put(listDataHeader.get(7), eighth);
        listDataChild.put(listDataHeader.get(8), nineth);
        listDataChild.put(listDataHeader.get(9), tenth);
        listDataChild.put(listDataHeader.get(10), eleventh);
        listDataChild.put(listDataHeader.get(11), twelvth);
    }


    @Override
    public void onResume() {

        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();

                    return true;
                }
                return false;
            }
        });
    }


}
