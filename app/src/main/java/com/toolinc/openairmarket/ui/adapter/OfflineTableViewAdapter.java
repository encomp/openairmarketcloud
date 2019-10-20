package com.toolinc.openairmarket.ui.adapter;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.common.room.converter.DateTimeConverter;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncState;
import com.toolinc.openairmarket.ui.view.tableview.model.CellModel;

/** */
public class OfflineTableViewAdapter
    implements Function<CollectionSyncState, ImmutableList<CellModel>> {

  @Override
  public ImmutableList<CellModel> apply(CollectionSyncState collectionSyncState) {
    return ImmutableList.<CellModel>builder()
        .add(CellModel.create(collectionSyncState.getId(), collectionSyncState.getId()))
        .add(CellModel.create(collectionSyncState.getId(), collectionSyncState.getNumberOfDocs()))
        .add(CellModel.create(collectionSyncState.getId(), collectionSyncState.getStatus().name()))
        .add(
            CellModel.create(
                collectionSyncState.getId(),
                DateTimeConverter.toString(collectionSyncState.getLastUpdate())))
        .build();
  }
}
