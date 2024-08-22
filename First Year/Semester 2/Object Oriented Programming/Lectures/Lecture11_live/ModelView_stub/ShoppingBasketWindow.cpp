#include "ShoppingBasketWindow.h"
#include <qsortfilterproxymodel.h>

ShoppingBasketWindow::ShoppingBasketWindow(QAbstractItemModel* model, QWidget *parent)
	: QWidget(parent)
{
	ui.setupUi(this);
	ui.productsTableView->setModel(model);

	connect(ui.addButton, &QPushButton::clicked,
		this, [&]() {
			Product p{"laptop", 3000, 1};
		// !!! 
		// will only work for the ProductsModel, not for the QSortFilterProxyModel (in main)
		// to make sure it works for the QSortFilterProxyModel, a custom proxy model
		// should be implemented, which offers the add functionality
		ProductsModel* prodModel = dynamic_cast<ProductsModel*>(model);
		if (prodModel != nullptr)
			prodModel->addProduct(p);
		});
}

ShoppingBasketWindow::~ShoppingBasketWindow()
{}
