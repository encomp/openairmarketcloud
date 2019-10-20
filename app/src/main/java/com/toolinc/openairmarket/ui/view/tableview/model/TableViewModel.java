package com.toolinc.openairmarket.ui.view.tableview.model;

import com.google.auto.value.AutoValue;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

/**
 * Table view model that populates a given {@link
 * com.toolinc.openairmarket.ui.view.tableview.adapter.TableViewAdapter}.
 */
@AutoValue
public abstract class TableViewModel {

  public abstract ImmutableList<ColumnHeaderModel> columnHeaderModels();

  public abstract ImmutableList<RowHeaderModel> rowHeaderModels();

  public abstract ImmutableList<List<CellModel>> cellModels();

  public static <T extends Serializable> Builder builder(
      Function<T, ImmutableList<CellModel>> transformation) {
    return new AutoValue_TableViewModel.Builder().setTransformation(transformation);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private Function<Serializable, ImmutableList<CellModel>> transformation;

    abstract ImmutableList.Builder<ColumnHeaderModel> columnHeaderModelsBuilder();

    abstract ImmutableList.Builder<RowHeaderModel> rowHeaderModelsBuilder();

    abstract ImmutableList.Builder<List<CellModel>> cellModelsBuilder();

    @SuppressWarnings("unchecked")
    public <T extends Serializable> Builder setTransformation(
        Function<T, ImmutableList<CellModel>> transformation) {
      this.transformation = (Function<Serializable, ImmutableList<CellModel>>) transformation;
      return this;
    }

    public Builder addColumnHeaderModel(String columnHeader) {
      columnHeaderModelsBuilder().add(ColumnHeaderModel.create(columnHeader));
      return this;
    }

    public Builder addAllColumnHeaderModel(List<String> columHeaders) {
      for (String header : columHeaders) {
        addColumnHeaderModel(header);
      }
      return this;
    }

    Builder addRowHeaderModel(String header) {
      rowHeaderModelsBuilder().add(RowHeaderModel.create(header));
      return this;
    }

    public Builder addAllRowHeaderModels(List<String> headers) {
      for (String header : headers) {
        addRowHeaderModel(header);
      }
      return this;
    }

    <T extends Serializable> Builder addCellModel(T cell) {
      cellModelsBuilder().add(transformation.apply(cell));
      return this;
    }

    public <T extends Serializable> Builder addAllCellModels(List<T> cells) {
      int i = 1;
      for (Serializable cell : cells) {
        addCellModel(cell);
        addRowHeaderModel("" + i);
        i++;
      }
      return this;
    }

    public abstract TableViewModel build();
  }
}
