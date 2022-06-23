# MinifyAtMax
Playground for understanding how pixels are calculate while image downscaling with different texture filtering types.  
You can run downscaling by click on button, file with result will save in standart `filesDir` with corresponding name.  
Also, after saving toast with name of file would be shown.  
You can experiment with your own images by loading it in resources and modify constants
```
        const val RESOURCE_ID = R.drawable.blue_reed_5_10
        const val SOURCE_IMAGE_SIZE = 10
```  

More details about pixel calculating are in [article](https://habr.com/ru/company/prequel/blog/672534/).
