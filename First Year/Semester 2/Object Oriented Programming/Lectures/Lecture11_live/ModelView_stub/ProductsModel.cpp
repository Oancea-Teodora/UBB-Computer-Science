#include "ProductsModel.h"

ProductsModel::ProductsModel(ShoppingBasket& basket): basket{basket}
{
}

int ProductsModel::rowCount(const QModelIndex& parent) const
{
	return basket.getSize();
}

int ProductsModel::columnCount(const QModelIndex& parent) const
{
	return 3;
}

QVariant ProductsModel::data(const QModelIndex& index, int role) const
{
	int row = index.row();
	int col = index.column();

	Product p = basket.getAll()[row];

	if (role == Qt::DisplayRole)
	{
		switch (col)
		{
		case 0:
			return QString::fromStdString(p.getName());
		case 1:
			return QString::number(p.getPrice());
		case 2:
			return QString::number(p.getQuantity());
		default:
			break;
		}
	}

	if (role == Qt::TextAlignmentRole)
	{
		if (row == 0 && col == 0)
			return Qt::AlignRight;
	}

	return QVariant();
}

QVariant ProductsModel::headerData(int section, Qt::Orientation orientation, int role) const
{
	if (role == Qt::DisplayRole && orientation == Qt::Horizontal)
	{
		switch (section)
		{
		case 0:
			return "Name";
		case 1:
			return "Price";
		case 2:
			return "Quantity";
		default:
			break;
		}
	}

	return QVariant();
}

Qt::ItemFlags ProductsModel::flags(const QModelIndex& index) const
{
	if (index.column() == 2)
		return Qt::ItemIsEnabled | Qt::ItemIsEditable;
	return Qt::ItemFlags();
}

bool ProductsModel::setData(const QModelIndex& index, const QVariant& value, int role)
{
	int row = index.row();
	int col = index.column();

	Product& product = this->basket.getAll()[row];

	if (role == Qt::EditRole)
		product.setQuantity(value.toDouble());

	emit dataChanged(index, index);

	return false;
}

void ProductsModel::addProduct(const Product& p)
{
	int lastRow = this->basket.getSize();
	this->beginInsertRows(QModelIndex{}, lastRow, lastRow);

	this->basket.addProduct(p);

	this->endInsertRows();
}
