package com.afollestad.silk.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.afollestad.silk.R;
import com.afollestad.silk.adapters.SilkAdapter;
import com.afollestad.silk.cache.SilkComparable;
import com.afollestad.silk.views.list.SilkGridView;
import com.afollestad.silk.views.list.SilkListView;

/**
 * A {@link com.afollestad.silk.fragments.SilkFragment} that shows a list, with an empty text, and has progress bar support. Has other various
 * convenience methods and handles a lot of things on its own to make things easy.
 * <p/>
 * The fragment uses a {@link com.afollestad.silk.adapters.SilkAdapter} to display items of type T.
 *
 * @param <T> The type of items held in the fragment's list.
 * @author Aidan Follestad (afollestad)
 */
public abstract class SilkListFragment<T extends SilkComparable> extends SilkFragment {

    private AbsListView mListView;
    private TextView mEmpty;
    private ProgressBar mProgress;
    private SilkAdapter<T> mAdapter;
    private boolean mLoading;

    /**
     * Gets the ListView contained in the Fragment's layout.
     */
    public final AbsListView getListView() {
        return mListView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = initializeAdapter();
        if (mAdapter == null) throw new RuntimeException("The SilkListFragment's adapter cannot be null.");
    }

    /**
     * Uses a list layout by default but this can be overridden if necessary. If you do override this method,
     * the returned layout must have the same views with the same IDs in addition to whatever you add or change.
     */
    @Override
    public int getLayout() {
        return R.layout.fragment_list;
    }

    @Override
    public String getTitle() {
        // This isn't needed but can be overridden by inheriting classes if needed.
        return null;
    }

    /**
     * Inheriting classes return a string resource for the list's empty text value here.
     * <p/>
     * The text will be shown when the list is not loading and the list is empty.
     */
    public abstract int getEmptyText();

    /**
     * Updates the edit text that was initially set to the value of {@link #getEmptyText()}.
     */
    public final void setEmptyText(CharSequence text) {
        mEmpty.setText(text);
    }

    /**
     * Gets the SilkAdapter used to add and remove items from the list.
     */
    public final SilkAdapter<T> getAdapter() {
        return mAdapter;
    }

    /**
     * Only called once to cause inheriting classes to create a new SilkAdapter that can later be retrieved using
     * {#getAdapter}.
     */
    protected abstract SilkAdapter<T> initializeAdapter();

    /**
     * Called when an item in the list is tapped by the user.
     *
     * @param index The index of the tapped item.
     * @param item  The actual tapped item from the adapter.
     * @param view  The view in the list that was tapped.
     */
    public abstract void onItemTapped(int index, T item, View view);

    /**
     * Called when an item in the list is long-tapped by the user.
     *
     * @param index The index of the long-tapped item.
     * @param item  The actual long-tapped item from the adapter.
     * @param view  The view in the list that was long-tapped.
     * @return Whether or not the event was handled.
     */
    public abstract boolean onItemLongTapped(int index, T item, View view);

    /**
     * Gets whether or not the list is currently loading.
     * <p/>
     * This value is changed using {#setLoading} and {#setLoadComplete}.
     */
    public final boolean isLoading() {
        return mLoading;
    }

    private void setListShown(boolean shown) {
        if (!shown) {
            mEmpty.setVisibility(View.GONE);
        } else {
            mListView.setEmptyView(mEmpty);
            getAdapter().notifyDataSetChanged();
        }
        mProgress.setVisibility(shown ? View.GONE : View.VISIBLE);
    }

    /**
     * Notifies the fragment that it is currently loading data.
     * <p/>
     * If true is passed as a parameter, the list or empty text will be hidden, and the progress view to be shown.
     *
     * @param progress Whether or not the progress view will be shown and the list will be hidden.
     */
    public final void setLoading(boolean progress) {
        if (progress)
            setListShown(false);
        mLoading = true;
    }

    /**
     * Notifies the fragment that it is done loading data. This causes the progress view to become invisible, and the list
     * or empty text become visible again.
     *
     * @param error Whether or not an error occurred while loading. This value is not used in the default implementation
     *              but can be used by overriding classes.
     */
    public void setLoadComplete(boolean error) {
        mLoading = false;
        setListShown(true);
    }

    /**
     * References to views are created here, along with hooks to event handlers. If you override this method in a sub-class,
     * make sure you make a call to the super method.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (AbsListView) view.findViewById(R.id.list);
        if (mListView instanceof SilkListView)
            ((SilkListView) mListView).setAdapter(mAdapter);
        else if (mListView instanceof SilkGridView)
            ((SilkGridView) mListView).setSilkAdapter(mAdapter);
        else mListView.setAdapter(mAdapter);
        mEmpty = (TextView) view.findViewById(R.id.empty);
        mListView.setEmptyView(mEmpty);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        if (getEmptyText() > 0)
            mEmpty.setText(getEmptyText());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                T item = getAdapter().getItem(index);
                onItemTapped(index, item, view);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
                T item = getAdapter().getItem(index);
                return onItemLongTapped(index, item, view);
            }
        });
    }
}