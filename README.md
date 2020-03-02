# Aplikasi MyGallery
Aplikasi yang digunakan untuk mempelajari bagaimana Android berkomunikasi menggunakan _**ContentProvider**_ untuk mendapatkan data dari _**Internal**_ maupun _**External Storage**_.

## Beberapa _class_ yang perlu diperhatikan
### **1. LoadAlbumTask**
Class ini berguna untuk melakukan _load_ data gambar dari _storage_ pengguna. Dibuat _AsyncTask_ karena ada kemungkinan file gambar dari pengguna banyak. Hal ini sangat berpotensi mengganggu jalannya _main thread_. Oleh karena itu dibuatlah proses _load_ di _thread_ lainnya.
### **2. AlbumAdapter**
Merupakan adapter utama yang nantinya digunakan untuk mengisi GridView yang sudah disediakan.
### **3. Function**
Class ini berisi fungsi-fungsi yang akan membantu proses pembuatan kode di class lainnya.

## Selamat mencoba.

## FTI - UKSW
