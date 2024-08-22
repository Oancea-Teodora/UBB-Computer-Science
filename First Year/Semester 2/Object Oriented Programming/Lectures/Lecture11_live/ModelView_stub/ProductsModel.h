#pragma once
#include <qabstractitemmodel.h>
#include "ShoppingBasket.h"

class ProductsModel :
    public QAbstractTableModel
{
private:
    ShoppingBasket& basket;

public:
    ProductsModel(ShoppingBasket& basket);

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;
    int columnCount(const QModelIndex& parent = QModelIndex()) const override;
    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const;
    Qt::ItemFlags flags(const QModelIndex& index) const override;
    bool setData(const QModelIndex& index, const QVariant& value, int role = Qt::EditRole) override;

    void addProduct(const Product& p);
};

