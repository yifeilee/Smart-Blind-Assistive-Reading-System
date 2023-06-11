from distutils.core import setup, Extension
 
 
mytext_module = Extension('_mytext',
                           sources=['mytext_wrap.cxx', 'mytext.cpp'],
                           )
 
setup (name = 'mytext',
       version = '0.1',
       author      = "zhang yun",
       description = """Convert English to a Braille index""",
       ext_modules = [mytext_module],
       py_modules = ["mytext"],
       )
