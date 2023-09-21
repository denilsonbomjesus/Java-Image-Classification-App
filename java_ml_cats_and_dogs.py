import numpy as np
import tensorflow as tf
import keras
from keras.preprocessing import image
import keras.utils as image
from keras.applications.mobilenet import preprocess_input
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import sys

def process_image(image_path):

    print("Processando a imagem:", image_path)
    
    # Carrega o modelo pré-treinado MobileNet com pesos da ImageNet e seus pesos treinados
    model = keras.models.load_model('/path/to/cats_and_dogs.keras')

    # Carrega e prepara uma nova imagem de teste
    test_image = image.load_img(image_path, target_size=(224, 224))
    test_image = image.img_to_array(test_image)
    test_image = preprocess_input(test_image)
    test_image = np.expand_dims(test_image, axis=0)

    # Realiza a predição da classe da nova imagem usando o modelo treinado
    result = model.predict(test_image)

    # Verifica o resultado da predição e imprime se é um cachorro ou um gato
    if result[0][0] > 0.5:
        prediction = 'dog'
    else:
        prediction = 'cat'

    # Retorne a classificação para a saída padrão
    print(prediction)


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python process_image.py <image_path>")
    else:
        image_path = sys.argv[1]
        process_image(image_path)