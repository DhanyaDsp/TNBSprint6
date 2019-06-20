package com.ey.dgs.notifications;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationListAdapter;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.notifications.settings.SettingsMenuFragment;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationListActivity extends AppCompatActivity implements SettingsMenuFragment.OnFragmentInteractionListener {
    public static final int READ_NOTIFICATION = 100;
    RecyclerView rvNotifications;
    NotificationListAdapter notificationListAdapter;
    ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationViewModel notificationViewModel;
    int userId;
    AppPreferences appPreferences;
    boolean allNotifications;
    String accountNumber;
    private Drawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUser_id();
        allNotifications = getIntent().getBooleanExtra("allNotifications", false);
        if (!allNotifications) {
            accountNumber = getIntent().getStringExtra("accountNumber");
        } else {
            accountNumber = "";
        }
        initViews();
        subscribe();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete:
                deleteAll();
                break;
            case R.id.action_notification_settings:
                Intent intent = new Intent(NotificationListActivity.this, NotificationSettingsActivity.class);
                intent.putExtra("allNotifications", allNotifications);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void subscribe() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.loadNotificationsFromLocalDB(userId, accountNumber);
        notificationViewModel.getNotifications().observe(this, notifications -> {
            this.notifications.clear();
            this.notifications.addAll(notifications);
            Collections.reverse(this.notifications);
            notificationListAdapter.notifyDataSetChanged();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                notificationViewModel.deleteNotification(notificationListAdapter.
                        getNotificationAt(viewHolder.getAdapterPosition()));
                notifications.remove(viewHolder.getAdapterPosition());
                notificationListAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                icon = ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.delete);
                drawOnSwipe(c, viewHolder, dX);

            }
        }).attachToRecyclerView(rvNotifications);
    }

    private void drawOnSwipe(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {

        ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.yellow1));
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.notifications));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setHasFixedSize(true);
        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        rvNotifications.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvNotifications.addItemDecoration(itemDecorator);
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
        notificationListAdapter = new NotificationListAdapter(this, notifications);
        rvNotifications.setAdapter(notificationListAdapter);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }

    private void deleteAll() {
        DialogHelper.showDeleteAll(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.hidePopup();
            }
        }, v -> {
            DialogHelper.hidePopup();
            if (!notifications.isEmpty()) {
                notificationViewModel.deleteNotificationsFromLocalDB();
                notifications.clear();
                notificationListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "No notifications to delete", Toast.LENGTH_SHORT).
                        show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_NOTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                Notification notification = (Notification) data.getSerializableExtra("notification");
                int position = data.getIntExtra("position", -1);
                if (position != -1 && notification != null) {
                    notifications.set(position, notification);
                    notificationListAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}