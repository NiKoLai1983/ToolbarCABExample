package es.jpv.android.examples.toolbarcabexample;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import es.jpv.android.examples.toolbarcabexample.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment
    implements RVAdapter.OnItemClickListener, RVAdapter.OnItemLongClickListener {

    ActionMode mActionMode;
    RVAdapter adapter;
    Integer selectedPosition = null;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Provide default implementation to return a simple list view.  Subclasses
     * can override to replace with their own layout.  If doing so, the
     * returned view hierarchy <em>must</em> have a ListView whose id
     * is {@link android.R.id#list android.R.id.list} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty}
     * that is to be shown when the list is empty.
     * <p/>
     * <p>If you are overriding this method with your own custom content,
     * consider including the standard layout {@link android.R.layout#list_content}
     * in your layout file, so that you continue to retain all of the standard
     * behavior of ListFragment.  In particular, this is currently the only
     * way to have the built-in indeterminant progress state be shown.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        adapter = new RVAdapter(getActivity(), DummyContent.ITEMS_CURSOR);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isInActionMode")) {
                ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
            }
        }
        return view;
    }

    @Override
    public void onItemClick(View v, int position) {
        if (isInActionMode) {
            mActionMode.finish();
        } else {
            Toast.makeText(getActivity(),
                    "Clicked " + ((TextView) v.findViewById(R.id.textView)).getText() +
                            " on position " + position,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemLongClick(View v, int position) {
        v.setSelected(true);
        selectedPosition = position;
        mActionMode = ((AppCompatActivity) getActivity())
                .startSupportActionMode(mActionModeCallback);
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isInActionMode", isInActionMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private boolean isInActionMode = false;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(R.string.options);
            mode.getMenuInflater().inflate(R.menu.item_fragment_cab, menu);
            isInActionMode = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    Toast.makeText(
                            getActivity(),
                            "Removing item in position " + selectedPosition,
                            Toast.LENGTH_SHORT)
                            .show();
                    Cursor cursor = DummyContent.deleteItem(selectedPosition);
                    adapter.setCursor(cursor);
                    adapter.notifyDataSetChanged();
                    mActionMode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isInActionMode = false;
            selectedPosition = null;
        }
    };

}
