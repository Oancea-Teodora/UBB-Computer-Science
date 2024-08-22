#pragma once

#include <QWidget>
#include "ui_ShoppingBasketWidget.h"
#include "ShoppingBasket.h"
#include "Observer.h"

class ShoppingBasketWidget : public QWidget, public Observer
{
	Q_OBJECT

public:
	ShoppingBasketWidget(ShoppingBasket& basket, QWidget *parent = Q_NULLPTR);
	~ShoppingBasketWidget();

private:
	Ui::ShoppingBasketWidget ui;
	ShoppingBasket& basket;
	double totalPrice;

public:
	void populateList();
	double computeTotalPrice();
	void update();
};
