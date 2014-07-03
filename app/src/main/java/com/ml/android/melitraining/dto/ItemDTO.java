package com.ml.android.melitraining.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 *
 "site_id": "MLA",
 "title": "Tv Lcd 19  Combo Monitor Lg + Sintonizadora Kworld Hd Ready",
 "subtitle": "Mipc- Garantía Oficial 3 Años En Monitores Lg - Envios A Todo El País",
 "seller_id": "25679280",
 "category_id": "MLA124100",
 "official_store_id": null,
 "price": 1832.99,
 "base_price": 1832.99,
 "original_price": null,
 "currency_id": "ARS",
 "initial_quantity": 121,
 "available_quantity": 97,
 "sold_quantity": 63,
 "buying_mode": "buy_it_now",
 "listing_type_id": "gold_premium",
 "start_time": "2013-12-30T13:02:17.000Z",
 "stop_time": "2014-05-01T12:16:17.000Z",
 "condition": "new",
 "permalink": "http://articulo.mercadolibre.com.ar/MLA-489810290-tv-lcd-19-combo-monitor-lg-sintonizadora-kworld-hd-ready-_JM",
 "thumbnail": "http://mla-s1-p.mlstatic.com/13032-MLA20070547136_032014-I.jpg",
 "secure_thumbnail": "https://a248.e.akamai.net/mla-s1-p.mlstatic.com/13032-MLA20070547136_032014-I.jpg",
 "pictures": - [
 - {
 "id": "13032-MLA20070547136_032014",
 "url": "http://mla-s1-p.mlstatic.com/13032-MLA20070547136_032014-O.jpg",
 "secure_url": "https://a248.e.akamai.net/mla-s1-p.mlstatic.com/13032-MLA20070547136_032014-O.jpg",
 "size": "500x500",
 "max_size": "1200x1200",
 "quality": "",
 },
 ],
 "video_id": null,
 "descriptions": - [
 - {
 "id": "MLA489810290-514843176",
 },
 ],
 "accepts_mercadopago": true,
 "non_mercado_pago_payment_methods": - [
 - {
 "id": "MLAMO",
 "description": "Efectivo",
 "type": "G",
 },
 ],
 "shipping": - {
 "mode": "not_specified",
 "local_pick_up": true,
 "free_shipping": false,
 "methods": [
 ],
 "dimensions": null,
 },
 "seller_address": - {
 "id": 103989249,
 "comment": "",
 "address_line": "",
 "zip_code": "",
 "city": - {
 "id": "TUxBQlJFQzkyMTVa",
 "name": "Recoleta",
 },
 "state": - {
 "id": "AR-C",
 "name": "Capital Federal",
 },
 "country": - {
 "id": "AR",
 "name": "Argentina",
 },
 "latitude": -34.59,
 "longitude": -58.38,
 "search_location": - {
 "neighborhood": - {
 "id": "TUxBQlJFQzkyMTVa",
 "name": "Recoleta",
 },
 "city": - {
 "id": "TUxBQ0NBUGZlZG1sYQ",
 "name": "Capital Federal",
 },
 "state": - {
 "id": "TUxBUENBUGw3M2E1",
 "name": "Capital Federal",
 },
 },
 },
 "seller_contact": null,
 "location": {
 },
 "geolocation": - {
 "latitude": -34.59,
 "longitude": -58.38,
 },
 "coverage_areas": [
 ],
 "attributes": [
 ],
 "listing_source": "",
 "variations": [
 ],
 "status": "active",
 "sub_status": [
 ],
 "tags": - [
 "good_quality_thumbnail",
 "dragged_bids_and_visits",
 ],
 "warranty": null,
 "catalog_product_id": null,
 "parent_item_id": "MLA474662213",
 "differential_pricing": null,
 "automatic_relist": false,
 "date_created": "2013-12-30T13:02:17.000Z",
 "last_updated": "2014-04-26T00:27:01.000Z",


 *
 * Created by marbarfa on 4/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
    public String id;

    public String site_id;
    public String title;
    public String subtitle;
    public String category_id;
    public Double price;
    public String quantity;
    public Date start_time;
    public Date stop_time;
    public String status;
    public String sold_quantity;
    public String condition;

    public String thumbnail;
    public List<PictureDTO> pictures;

}
