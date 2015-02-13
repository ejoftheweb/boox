<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
		<head>
		<link href="http://www.lucadi.co.uk/styles/invoice.css" type="stylesheet/css"/>
		</head>
		<body class="invoice">
		<h1 class="invoice-h1">Invoice <xsl:value-of select="info/invoice-number"/></h1>
		<p class="invoice-date">Date: <xsl:value-of select="info/invoice-date"/></p>
		<h2>Items</h2>
		<table class="invoice_items">
		<tr class="items_table_head">
			<th>#</th>
			<th>Item</th>
			<th>Quantity</th>
			<th>Unit Price</th>
			<th>Discount</th>
			<th>Net</th>
			<th>Tax</th>
			<th>Total</th>
		</tr>
		<xsl:for-each select="items/item">
		<tr>
			<td><xsl:value-of select="line-number"/></td>
			<td><xsl:value-of select="description"/></td>
			<td><xsl:value-of select="quantity"/></td>
			<td><xsl:value-of select="unit-price"/></td>
			<td><xsl:value-of select="discount"/></td>
			<td><xsl:value-of select="net"/></td>
			<td><xsl:value-of select="tax"/></td>
			<td><xsl:value-of select="gross"/></td>
		</tr>
		</xsl:for-each>
		<tr style="totals">
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td style="totals"><xsl:value-of select= "totals/net"/></td>
			<td style="totals"><xsl:value-of select= "totals/tax"/></td>
			<td style="totals">£<xsl:value-of select= "totals/gross"/></td>
		</tr>
		<div class="terms-footer">
		<xsl:for-each select="terms/term"/>
		  <p class="invoice-term"><xsl:value-of select="</p>
		</div>
		</table>
		</body>
		</html>
	</xsl:template>
</xsl:stylesheet>