#pragma once

#include <QWidget>
#include "ui_ShoppingBasketWindow.h"
#include "ProductsModel.h"

class ShoppingBasketWindow : public QWidget
{
	Q_OBJECT

public:
	ShoppingBasketWindow(QAbstractItemModel* model, QWidget *parent = nullptr);
	~ShoppingBasketWindow();

private:
	Ui::ShoppingBasketWindowClass ui;
};
