**IAM Service**



Service ini ditujukan untuk simulasi identity access management application.

Saat pertama kali jalankan aplikasi ini, harap untuk execute script database init dan seed untuk inisiasi data yang akan digunakan



Secara keseluruhan service ini memiliki beberapa api



* POST /auth/login

endpoint ini endpoint paling awal untuk mengambil kredensial user yang terdaftar, dikarenakan dari endpoint ini akan menghasilkan token, dan seluruh endpoint selain ini membutuhkan token sebagai pengenal kredensial. Masukkan username yang terdaftar pada table user, dan input password sebagai berikut 'P@ssw0rd' untuk pass default seluruh akun.



* GET /accesses/get

endpoint ini ditujukan untuk melihat seluruh akses yang tersedia dari sistem ini, tanpa melihat siapa yang memiliki akses.



* GET /accesses/get/me

endpoint ini ditujukan untuk melihat akses apa saja yang dimiliki oleh user yang sedang login saat ini.



* POST /requests

endpoint ini ditujukan sebagai pintu masuk formulir perubahan akses user. Bila user ingin ada perubahan dalam akses yang dia miliki, entah itu ingin dihilangkan maupun ditambahkan, bisa melalui endpoint ini.

ketentuan untuk bisa melakukan pengajuan update akses yaitu :

1. user tidak boleh ada request yang sedang berjalan atau tahap approval oleh manager atau admin.
2. user bisa mengajukan lebih dari 1 akses
3. jika user ingin menambahkan sebuah akses, maka akses yang saat ini tidak boleh terdaftar untuk menghindari duplikasi
4. jika user ingin mencabut akses, maka akses tersebut harus dimiliki terlebih dahulu oleh user yang saat ini sedang login.



* GET /request/me?size=5\&page=1

endpoint ini ditujukan untuk melihat atau tracing request yang sudah pernah atau sedang diajukan oleh user yang sedang login saat ini.



* GET /approvals

endpoint ini ditujukan untuk melihat approval apa saja yang sedang menunggu antrian approve oleh user. Terlepas oleh user ini manager atau admin.

beberapa ketentuan ditambahkan pada endpoint ini yaitu :

1. list yang ditampilkan hanyalah pengajuan yang menunjuk langsung user ini (apabila dia adalah manajer sebuah user lain)
2. list yang ditampilkan juga mengeluarkan pengajuan yang menunjuk secara global user yang sedang login saat ini
3. dikarenakan admin dan manager sama sama bisa melakukan pengajuan, maka didalam list ini akan menghindari pengajuan akses yang dilakukan oleh diri sendiri meskipun dia memiliki role untuk melakukan approval.



* PATCH /approvals/{id\_request}/action

endpoint ini ditujukan untuk update pengajuan yang sedang berjalan. berikut adalah ketetuan ketentuan dari endpoint ini

1. user yang bisa approve hanyalah user yang memang ditunjuk langsung sebagai manajer atau memiliki role yang ditujukan
2. hierarki penunjukan manager langsung lebih tinggi daripada penunjukkan role, maka dari itu apabila ada request yang langsung ditujukan kepada manajerA karena manajerA adalah manajer dari userB, maka meskipun manajerC memiliki role manajer tidak bisa approve pengajuan ini
3. hanya ada dua aksi didalam endpoint ini yaitu approve atau reject, diluar itu akan dianggap anomali dan mengakibatkan bad request
4. jika pengajuan masih didalam tahap pending manager lalu user approve, maka akan diteruskan ke pending admin. dalam kasus ini seluruh akses belum ada perubahan
5. jika pengajuan sedang dalam tahap pending admin dan admin menyetujui pengajuan ini, seluuruh permintaan akses akan diaplikasikan baik itu penambahan maupun penghapusan akses.
6. jika pengajuan ini di reject, baik saat pending manager atau admin maka pengajuan akan langsung di close.



* GET /dashboard/metrics

ini adalah endpoint dashboard untuk melihat seluruh data yang sedang berjalan pada aplikasi ini.





\-------------------------------



**collections API sudah ada didalam project ini**, nama file -> iam\_service\_daffa\_collections.json di root project



**ERD sudah ada di dalam project ini,** nama file -> iam\_service\_erd.drawio



**Berikut adalah link evidence aplikasi berjalan** -> https://drive.google.com/file/d/1OHgJOVHMwf8KKord8Gca2MQiLD79iA7O/view?usp=sharing



